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
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;

class DomSemiStructuredDdiProfile implements Profile
{
	private final XMLDocument document;
	private List<Constraint> constraints;
	private String profileName;

	DomSemiStructuredDdiProfile( XMLDocument document ) throws IOException, NotDocumentException
	{
		try
		{
			this.document = document;
			parseProfileName();

			constraints = new ArrayList<>();
			for ( Node usedNode : document.selectNodes( "/DDIProfile/Used" ) )
			{
				constraints.add( new CompilableXPathConstraint( getLocationPath( usedNode ) ) );
				constraints.add( new PredicatelessXPathConstraint( getLocationPath( usedNode ) ) );
				parseControlledVocabularyRepositoryConstraint( usedNode );

				parseMandatoryNodeConstraint( usedNode );
				parseOptionalNodeConstraint( usedNode );
				parseNotBlankNodeConstraint( usedNode );
				parseMaximumElementOccurrenceConstraint( usedNode );
				parseMandatoryNodeIfParentPresentConstraint( usedNode );
				parseFixedValueNodeConstraint( usedNode );

				parseCodeValueOfControlledVocabularyConstraint( usedNode );
				parseDescriptiveTermOfControlledVocabularyConstraint( usedNode );
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

		this.constraints = new ArrayList<>(orderedConstraints);
	}

	private static String getLocationPath( Node usedNode )
	{
		return usedNode.getAttributes().getNamedItem( "xpath" ).getTextContent();
	}

	private void parseProfileName() throws XPathExpressionException
	{
		Node nameNode = document.selectNode( "/DDIProfile/DDIProfileName" );
		if ( nameNode != null )
		{
			profileName = nameNode.getTextContent();
		}
	}

	private Optional<XMLDocument> findExtension( Node usedNode ) throws IOException, SAXException, XPathExpressionException
	{
		String extensionRecognizingXPath = "Instructions/Content[contains(.,'<Constraints>')]";
		Node constraintsNode = document.selectNode( usedNode, extensionRecognizingXPath );
		if ( constraintsNode != null )
		{
			XMLDocument extension = XMLDocument.newBuilder().source( constraintsNode.getTextContent() ).build();
			return Optional.of( extension );
		}
		else
		{
			return Optional.empty();
		}
	}

	private void parseMandatoryNodeConstraint( Node usedNode )
	{
		Node isRequiredNode = usedNode.getAttributes().getNamedItem( "isRequired" );
		if ( isRequiredNode != null && isRequiredNode.getNodeValue().equalsIgnoreCase( "true" ) )
		{
			constraints.add( new MandatoryNodeConstraint( getLocationPath( usedNode ) ) );
		}
	}

	private void parseFixedValueNodeConstraint( Node usedNode )
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

	private void parseOptionalNodeConstraint( Node usedNode ) throws IOException, SAXException, XPathExpressionException
	{
		Attr isRequiredNode = (Attr) usedNode.getAttributes().getNamedItem( "isRequired" );

		if ( isRequiredNode != null && Boolean.parseBoolean( isRequiredNode.getValue() ) )
		{
			return;
		}

		if ( document.selectNode( usedNode, "Description[Content='Required: Recommended']" ) != null
				|| document.selectNode( usedNode, "Description[Content='Recommended']" ) != null
				|| hasRecommendedNodeConstraintExtension( usedNode ) )
		{
			constraints.add( new RecommendedNodeConstraint( getLocationPath( usedNode ) ) );
		}
		else
		{
			constraints.add( new OptionalNodeConstraint( getLocationPath( usedNode ) ) );
		}
	}

	@SuppressWarnings( "squid:S1075" )
	private boolean hasRecommendedNodeConstraintExtension( Node usedNode ) throws IOException, SAXException, XPathExpressionException
	{
		Optional<XMLDocument> extensionOpt = findExtension( usedNode );
		if (extensionOpt.isPresent())
		{
			XMLDocument extension = extensionOpt.get();
			String locationPath = "/Constraints/RecommendedNodeConstraint";
			return !extension.selectNodes( locationPath ).isEmpty();
		}
		return false;
	}

	@SuppressWarnings( "squid:S1075" )
	private void parseCodeValueOfControlledVocabularyConstraint( Node usedNode ) throws IOException, SAXException, XPathExpressionException
	{
		Optional<XMLDocument> extensionOpt = findExtension( usedNode );
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
	private void parseDescriptiveTermOfControlledVocabularyConstraint( Node usedNode ) throws IOException, SAXException, XPathExpressionException
	{
		Optional<XMLDocument> extensionOpt = findExtension( usedNode );
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
	private void parseControlledVocabularyRepositoryConstraint( Node usedNode ) throws IOException, SAXException, XPathExpressionException, URISyntaxException
	{
		Optional<XMLDocument> extensionOpt = findExtension( usedNode );
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

	private void parseMaximumElementOccurrenceConstraint( Node usedNode )
	{
		Node limitMaxOccursNode = usedNode.getAttributes().getNamedItem( "limitMaxOccurs" );
		if ( limitMaxOccursNode != null )
		{
			constraints.add( new MaximumElementOccurrenceConstraint(
					getLocationPath( usedNode ),
					Long.parseLong( limitMaxOccursNode.getNodeValue() ) ) );
		}
	}

	@SuppressWarnings( "squid:S1075" )
	private void parseMandatoryNodeIfParentPresentConstraint( Node usedNode ) throws IOException, SAXException, XPathExpressionException
	{
		Optional<XMLDocument> extensionOpt = findExtension( usedNode );
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
	private void parseNotBlankNodeConstraint( Node usedNode ) throws IOException, SAXException, XPathExpressionException
	{
		Optional<XMLDocument> extensionOpt = findExtension( usedNode );
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
	public List<Constraint> getConstraints()
	{
		return unmodifiableList( constraints );
	}

	public eu.cessda.cmv.core.mediatype.profile.Profile toJaxbProfileV0()
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

			eu.cessda.cmv.core.mediatype.profile.Constraint jaxbConstraint = null;

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
            else if ( constraint instanceof RecommendedNodeConstraint )
            {
                jaxbConstraint = new eu.cessda.cmv.core.mediatype.profile.RecommendedNodeConstraint( locationPath );
            }
            else if ( constraint instanceof OptionalNodeConstraint )
            {
                jaxbConstraint = new eu.cessda.cmv.core.mediatype.profile.OptionalNodeConstraint( locationPath );
            }

			if (jaxbConstraint != null)
			{
				jaxbConstraints.add( jaxbConstraint );
			}
		}

		return jaxbProfile;
	}
}
