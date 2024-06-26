/*-
 * #%L
 * cmv-core
 * %%
 * Copyright (C) 2020 - 2021 CESSDA ERIC
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package eu.cessda.cmv.core;

import org.gesis.commons.xml.XMLDocument;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static java.util.Collections.unmodifiableSet;

class DomSemiStructuredDdiProfile extends AbstractProfile
{

	DomSemiStructuredDdiProfile( XMLDocument document ) throws IOException, NotDocumentException
	{
		super(parseProfileName( document ), parseProfileVersion( document ), new LinkedHashSet<>());
		try
		{
			for ( Node usedNode : document.selectNodes( "/DDIProfile/Used" ) )
			{
				if (!(usedNode instanceof Element))
				{
					continue;
				}
				Element usedElement = (Element) usedNode;

				constraints.add( new CompilableXPathConstraint( getLocationPath( usedElement ) ) );
				constraints.add( new PredicatelessXPathConstraint( getLocationPath( usedElement ) ) );
				parseControlledVocabularyRepositoryConstraint( document, usedElement );

				parseMandatoryNodeConstraint( usedElement );
				parseOptionalNodeConstraint( document, usedElement );
				parseNotBlankNodeConstraint( document, usedElement );
				parseMaximumElementOccurrenceConstraint( usedElement );
				parseMandatoryNodeIfParentPresentConstraint( document, usedElement );
				parseFixedValueNodeConstraint( usedElement );

				parseCodeValueOfControlledVocabularyConstraint( document, usedElement );
				parseDescriptiveTermOfControlledVocabularyConstraint( document, usedElement );
			}
		}
		catch ( SAXException | URISyntaxException e )
		{
			throw new NotDocumentException( e );
		}
		catch ( XPathExpressionException e )
		{
			// given that all the XPaths are hardcoded, this should never be thrown
			throw new IllegalStateException( e );
		}

		if ( constraints.isEmpty() )
		{
			throw new IllegalArgumentException( "Profile defines no constraints" );
		}

		// Reorder the constraints so that ControlledVocabularyRepositoryConstraints are first
		ArrayDeque<Constraint> orderedConstraints = new ArrayDeque<>( constraints.size() );

		for ( Constraint c : constraints )
		{
			if ( c instanceof ControlledVocabularyRepositoryConstraint )
			{
				orderedConstraints.addFirst( c );
			}
			else
			{
				orderedConstraints.addLast( c );
			}
		}

		// Clear the constraints list and add in the ordered constraints
		this.constraints.clear();
		this.constraints.addAll( orderedConstraints );
	}

	private static String getLocationPath( Element usedNode )
	{
		Attr xpath = usedNode.getAttributeNode( "xpath" );
		if (xpath != null)
		{
			return xpath.getTextContent();
		}
		else
		{
			return null;
		}
	}

	static String parseProfileName( XMLDocument document )
	{
		Node nameNode;
		try
		{
			nameNode = document.selectNode( "/DDIProfile/DDIProfileName" );
		}
		catch ( XPathExpressionException e )
		{
			throw new IllegalStateException(e);
		}

		if ( nameNode != null )
		{
			return nameNode.getTextContent().trim();
		}
		else
		{
			return null;
		}
	}

	static String parseProfileVersion( XMLDocument document )
    {
		Node versionNode;
		try
		{
			versionNode = document.selectNode( "/DDIProfile/Version" );
		}
		catch ( XPathExpressionException e )
		{
			throw new IllegalStateException(e);
		}

		if ( versionNode != null )
		{
			return versionNode.getTextContent().trim();
		}
		else
		{
			return null;
		}
	}

	private static Optional<XMLDocument> findExtension( XMLDocument document, Node usedNode ) throws IOException, SAXException, XPathExpressionException
	{
		String extensionRecognizingXPath = "Instructions/Content[contains(.,'<Constraints>')]";
		Node constraintsNode = document.selectNode( usedNode, extensionRecognizingXPath );
		if ( constraintsNode != null )
		{
			StringReader constraintReader = new StringReader( constraintsNode.getTextContent() );
			InputSource inputSource = new InputSource( constraintReader );
			XMLDocument extension = XMLDocument.newBuilder().build( inputSource );
			return Optional.of( extension );
		}
		else
		{
			return Optional.empty();
		}
	}

	private void parseMandatoryNodeConstraint( Element usedNode )
	{
		Node isRequiredNode = usedNode.getAttributes().getNamedItem( "isRequired" );
		if ( isRequiredNode != null && isRequiredNode.getNodeValue().equalsIgnoreCase( "true" ) )
		{
			constraints.add( new MandatoryNodeConstraint( getLocationPath( usedNode ) ) );
		}
	}

	private void parseFixedValueNodeConstraint( Element usedNode )
	{
		Node fixedValueNode = usedNode.getAttributes().getNamedItem( "fixedValue" );
		if ( fixedValueNode != null && Boolean.parseBoolean( fixedValueNode.getNodeValue() ) )
		{
			Node defaultValueNode = usedNode.getAttributes().getNamedItem( "defaultValue" );
			// TODO Find similar or generalized constraint like
			// MandatoryNodeIfParentPresentConstraint
			// defaultValue attribute is mandatory if fixedValue attribute is present
			if ( defaultValueNode != null )
			{
				String fixedValue = defaultValueNode.getTextContent().trim();
				Constraint constraint = new FixedValueNodeConstraint( getLocationPath( usedNode ), fixedValue );
				constraints.add( constraint );
				// TODO jaxbProfile.getConstraints().add( ... ) );
			}
		}
	}

	private void parseOptionalNodeConstraint( XMLDocument document, Element usedNode ) throws IOException, SAXException, XPathExpressionException
	{
		Attr isRequiredNode = usedNode.getAttributeNode( "isRequired" );

		if ( isRequiredNode != null && Boolean.parseBoolean( isRequiredNode.getValue() ) )
		{
			return;
		}

		if ( document.selectNode( usedNode, "Description[Content='Required: Recommended']" ) != null
				|| document.selectNode( usedNode, "Description[Content='Recommended']" ) != null
				|| hasRecommendedNodeConstraintExtension( document, usedNode ) )
		{
			constraints.add( new RecommendedNodeConstraint( getLocationPath( usedNode ) ) );
		}
		else
		{
			constraints.add( new OptionalNodeConstraint( getLocationPath( usedNode ) ) );
		}
	}

	@SuppressWarnings( "squid:S1075" )
	private boolean hasRecommendedNodeConstraintExtension( XMLDocument document, Node usedNode ) throws IOException, SAXException, XPathExpressionException
	{
		Optional<XMLDocument> extensionOpt = findExtension( document, usedNode );
		if (extensionOpt.isPresent())
		{
			XMLDocument extension = extensionOpt.get();
			String locationPath = "/Constraints/RecommendedNodeConstraint";
			return !extension.selectNodes( locationPath ).isEmpty();
		}
		return false;
	}

	@SuppressWarnings( "squid:S1075" )
	private void parseCodeValueOfControlledVocabularyConstraint( XMLDocument document, Element usedNode ) throws IOException, SAXException, XPathExpressionException
	{
		Optional<XMLDocument> extensionOpt = findExtension( document, usedNode );
		if (extensionOpt.isPresent())
		{
			XMLDocument extension = extensionOpt.get();
			String locationPath = "/Constraints/CodeValueOfControlledVocabularyConstraint";
			int nodeCount = extension.selectNodes( locationPath ).size();
			for ( int i = 0; i < nodeCount; i++ )
			{
				constraints.add( new CodeValueOfControlledVocabularyConstraint( getLocationPath( usedNode ) ) );
			}
		}
	}

	@SuppressWarnings( "squid:S1075" )
	private void parseDescriptiveTermOfControlledVocabularyConstraint( XMLDocument document, Element usedNode ) throws IOException, SAXException, XPathExpressionException
	{
		Optional<XMLDocument> extensionOpt = findExtension( document, usedNode );
		if (extensionOpt.isPresent())
		{
			XMLDocument extension = extensionOpt.get();
			String locationPath = "/Constraints/DescriptiveTermOfControlledVocabularyConstraint";
			int nodeCount = extension.selectNodes( locationPath ).size();
			for ( int i = 0; i < nodeCount; i++ )
			{
				constraints.add( new DescriptiveTermOfControlledVocabularyConstraint( getLocationPath( usedNode ) ) );
			}
		}
	}

	@SuppressWarnings( "squid:S1075" )
	private void parseControlledVocabularyRepositoryConstraint( XMLDocument document, Element usedNode ) throws IOException, SAXException, XPathExpressionException, URISyntaxException
	{
		Optional<XMLDocument> extensionOpt = findExtension( document, usedNode );
		if (extensionOpt.isPresent())
		{
			XMLDocument extension = extensionOpt.get();
			String locationPath = "/Constraints/ControlledVocabularyRepositoryConstraint";
			for ( Node constraintNode : extension.selectNodes( locationPath ) )
			{
				Constraint constraint = new ControlledVocabularyRepositoryConstraint(
						getLocationPath( usedNode ),
						extension.selectNode( constraintNode, "./RepositoryType" ).getTextContent(),
						new URI( extension.selectNode( constraintNode, "./RepositoryUri" ).getTextContent() ) );
				constraints.add( constraint );
			}
		}
	}

	private void parseMaximumElementOccurrenceConstraint( Element usedNode )
	{
		Attr limitMaxOccursNode = usedNode.getAttributeNode( "limitMaxOccurs" );
		if ( limitMaxOccursNode != null )
		{
			constraints.add( new MaximumElementOccurrenceConstraint(
					getLocationPath( usedNode ),
					Long.parseLong( limitMaxOccursNode.getNodeValue() ) ) );
		}
	}

	@SuppressWarnings( "squid:S1075" )
	private void parseMandatoryNodeIfParentPresentConstraint( XMLDocument document, Element usedNode ) throws IOException, SAXException, XPathExpressionException
	{
		Optional<XMLDocument> extensionOpt = findExtension( document, usedNode );
		if (extensionOpt.isPresent())
		{
			XMLDocument extension = extensionOpt.get();
			String locationPath = "/Constraints/MandatoryNodeIfParentPresentConstraint";
			if ( !extension.selectNodes( locationPath ).isEmpty() )
			{
				Constraint constraint = new MandatoryNodeIfParentPresentConstraint( getLocationPath( usedNode ) );
				constraints.add( constraint );
			}
		}
	}

	@SuppressWarnings( "squid:S1075" )
	private void parseNotBlankNodeConstraint( XMLDocument document, Element usedNode ) throws IOException, SAXException, XPathExpressionException
	{
		Optional<XMLDocument> extensionOpt = findExtension( document, usedNode );
		if (extensionOpt.isPresent())
		{
			XMLDocument extension = extensionOpt.get();
			String locationPath = "/Constraints/NotBlankNodeConstraint";
			if ( !extension.selectNodes( locationPath ).isEmpty() )
			{
				Constraint constraint = new NotBlankNodeConstraint( getLocationPath( usedNode ) );
				constraints.add( constraint );
			}
		}
	}

	@Override
	public Set<Constraint> getConstraints()
	{
		return unmodifiableSet( constraints );
	}

	@Override
	public String getProfileName()
	{
		return profileName;
	}

	@Override
	public String getProfileVersion()
	{
		return profileVersion;
	}

	eu.cessda.cmv.core.mediatype.profile.Profile toJaxbProfile()
	{
		eu.cessda.cmv.core.mediatype.profile.Profile jaxbProfile = new eu.cessda.cmv.core.mediatype.profile.Profile();
		List<eu.cessda.cmv.core.mediatype.profile.Constraint> jaxbConstraints = jaxbProfile.getConstraints();

		// Set the name of the profile
		jaxbProfile.setName( profileName );

		for ( Constraint constraint : constraints )
		{
			String locationPath = null;
			if (constraint instanceof NodeConstraint)
			{
				locationPath = ( (NodeConstraint) constraint ).getLocationPath();
			}

			eu.cessda.cmv.core.mediatype.profile.Constraint jaxbConstraint;

            if ( constraint instanceof CompilableXPathConstraint )
            {
                jaxbConstraint = new eu.cessda.cmv.core.mediatype.profile.CompilableXPathConstraint( locationPath );
            }
			else if ( constraint instanceof PredicatelessXPathConstraint )
            {
                jaxbConstraint = new eu.cessda.cmv.core.mediatype.profile.PredicatelessXPathConstraint( locationPath );
            }
            else if ( constraint instanceof MaximumElementOccurrenceConstraint )
            {
                jaxbConstraint = new eu.cessda.cmv.core.mediatype.profile.MaximumElementOccurrenceConstraint( locationPath, ( (MaximumElementOccurrenceConstraint) constraint ).getMaxOccurs() );
            }
			else if ( constraint instanceof FixedValueNodeConstraint )
			{
				jaxbConstraint = new eu.cessda.cmv.core.mediatype.profile.FixedValueNodeConstraint( locationPath, ( (FixedValueNodeConstraint) constraint ).getFixedValue() );
			}
			else if ( constraint instanceof NotBlankNodeConstraint )
			{
				jaxbConstraint = new eu.cessda.cmv.core.mediatype.profile.NotBlankNodeConstraint( locationPath );
			}
			else if (constraint instanceof MandatoryNodeConstraint )
			{
				jaxbConstraint = new eu.cessda.cmv.core.mediatype.profile.MandatoryNodeConstraint( locationPath );
			}
			else if ( constraint instanceof MandatoryNodeIfParentPresentConstraint )
			{
				jaxbConstraint = new eu.cessda.cmv.core.mediatype.profile.MandatoryNodeIfParentPresentConstraint( locationPath );
			}
            else if ( constraint instanceof RecommendedNodeConstraint )
            {
                jaxbConstraint = new eu.cessda.cmv.core.mediatype.profile.RecommendedNodeConstraint( locationPath );
            }
            else if ( constraint instanceof OptionalNodeConstraint )
            {
                jaxbConstraint = new eu.cessda.cmv.core.mediatype.profile.OptionalNodeConstraint( locationPath );
            }
			else
			{
				// Ideally this should never be reached, but it is useful in testing to catch any unexpected constraints
				throw new IllegalStateException( "Unexpected constraint " + constraint.getClass() );
			}

            jaxbConstraints.add( jaxbConstraint );
        }

		return jaxbProfile;
	}
}
