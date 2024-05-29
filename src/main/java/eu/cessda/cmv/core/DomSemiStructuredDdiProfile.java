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

import eu.cessda.cmv.core.mediatype.profile.PrefixMap;
import org.gesis.commons.xml.XMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

class DomSemiStructuredDdiProfile extends AbstractProfile
{
	private static final Logger log = LoggerFactory.getLogger( DomSemiStructuredDdiProfile.class );

	DomSemiStructuredDdiProfile( XMLDocument document ) throws IOException, NotDocumentException
	{
		super(parseProfileName( document ), parseProfileVersion( document ), new LinkedHashSet<>(), bindNamespacesToPrefixes( document ));
		try
		{
			parseXPathVersion( document );

			for ( Node usedNode : document.selectNodes( "/pr:DDIProfile/pr:Used" ) )
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

	static CMVNamespaceContext getProfileNamespaceContext()
	{
		CMVNamespaceContext nsContext = new CMVNamespaceContext();
		nsContext.bindNamespaceURI( "pr", "ddi:ddiprofile:3_2" );
		nsContext.bindNamespaceURI( "r", "ddi:reusable:3_2" );
		return nsContext;
	}

	private static CMVNamespaceContext bindNamespacesToPrefixes( XMLDocument document ) throws NotDocumentException
	{
		CMVNamespaceContext namespaceContext = new CMVNamespaceContext();

		// Discover namespace of target documents
		List<Node> nodes;
		try
		{
			nodes = document.selectNodes( "/pr:DDIProfile/pr:XMLPrefixMap" );
		}
		catch ( XPathExpressionException e )
		{
			throw new IllegalArgumentException( e );
		}

		for ( Node prefixNode : nodes )
		{
			if (!(prefixNode instanceof Element))
			{
				continue;
			}
			Element prefixElement = (Element) prefixNode;

			String prefix = null;
			String namespace = null;

			// Extract the namespace and prefix from the map
			NodeList childNodes = prefixElement.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++ )
			{
				Node childNode = childNodes.item( i );
				if ( "pr:XMLPrefix".equals( childNode.getNodeName() ) )
				{
					prefix = childNode.getTextContent();
				}
				else if ( "pr:XMLNamespace".equals( childNode.getNodeName() ) )
				{
					namespace = childNode.getTextContent();
				}
			}

			// TODO: validate prefix and namespace
			try
			{
				namespaceContext.bindNamespaceURI( prefix, namespace );
			}
			catch ( IllegalArgumentException e )
			{
				throw new NotDocumentException( "Failed to bind XMLPrefixMap namespaces: " + e.getMessage(), e );
			}
		}

		return namespaceContext;
	}

	private static void parseXPathVersion( XMLDocument document ) throws XPathExpressionException, NotDocumentException
	{
		// Check XPath specification version
		Node xpathNode = document.selectNode( "/pr:DDIProfile/pr:XPathVersion" );
		if ( xpathNode != null )
		{
			String xpathVersion = xpathNode.getTextContent().trim();
			if ( !xpathVersion.equals( "1.0" ) )
			{
				// Only version 1.0 is supported
				throw new NotDocumentException( "XPathVersion \"" + xpathVersion + "\" not supported. Supported XPath versions are \"1.0\"" );
			}
		}
		else
		{
			log.warn( "XPathVersion is not specified, defaulting to 1.0" );
		}
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
			nameNode = document.selectNode( "/pr:DDIProfile/pr:DDIProfileName" );
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
			versionNode = document.selectNode( "/pr:DDIProfile/r:Version" );
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
		String extensionRecognizingXPath = "pr:Instructions/r:Content[contains(.,'<Constraints>')]";
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

		if ( document.selectNode( usedNode, "pr:Description[r:Content='Required: Recommended']" ) != null
				|| document.selectNode( usedNode, "pr:Description[r:Content='Recommended']" ) != null
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

	eu.cessda.cmv.core.mediatype.profile.Profile toJaxbProfile()
	{
		eu.cessda.cmv.core.mediatype.profile.Profile jaxbProfile = new eu.cessda.cmv.core.mediatype.profile.Profile();
		List<eu.cessda.cmv.core.mediatype.profile.Constraint> jaxbConstraints = jaxbProfile.getConstraints();

		// Set the name and version of the profile
		jaxbProfile.setName( profileName );
		jaxbProfile.setVersion( profileVersion );

		// Set the namespace context
		List<PrefixMap> prefixMaps = new ArrayList<>();

		// This will always be an instance of CMVNamespaceContext, so this is a safe cast
		CMVNamespaceContext namespaceContext = (CMVNamespaceContext) getNamespaceContext();
		for( Map.Entry<String, String> binding : namespaceContext.getAllBindings().entrySet() )
		{
			PrefixMap prefixMap = new PrefixMap();
			prefixMap.setPrefix( binding.getKey() );
			prefixMap.setNamespace( binding.getValue() );
			prefixMaps.add( prefixMap );
		}
		jaxbProfile.setPrefixMap(prefixMaps);

		// Set constraints
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
