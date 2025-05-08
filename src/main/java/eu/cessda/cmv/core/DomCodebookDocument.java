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

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepository;
import org.gesis.commons.xml.XMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPathExpressionException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

class DomCodebookDocument implements Document
{
	private static final Logger LOGGER = LoggerFactory.getLogger( DomCodebookDocument.class );

	private final URI uri;
	private final XMLDocument document;
	private final Map<URI, ControlledVocabularyRepository> controlledVocabularyRepositoryMap;

	DomCodebookDocument( URI uri, XMLDocument document )
    {
		this( uri, document, new HashMap<>() );
	}

	DomCodebookDocument( URI uri, XMLDocument document, Map<URI, ControlledVocabularyRepository> cvrMap )
	{
		this.uri = uri;
		this.controlledVocabularyRepositoryMap = cvrMap;
		this.document = document;
	}

	@Override
	public URI getURI()
	{
		return uri;
	}

	public void setNamespaceContext( NamespaceContext namespaceContext )
	{
		document.setNamespaceContext( namespaceContext );
	}

	@Override
	public List<Node> getNodes( String locationPath ) throws XPathExpressionException
	{
		requireNonNull( locationPath );

		List<Node> nodes = new ArrayList<>();
		for ( org.w3c.dom.Node domNode : document.selectNodes( locationPath ) )
		{
			NodeImpl node;

			if ( "anlyUnit".equals( domNode.getLocalName() ) || "concept".equals( domNode.getLocalName() ) )
			{
				ControlledVocabularyRepository repository = null;

				try
				{
					URI vocabURI = getVocabURI( (Element) domNode );
					if (vocabURI != null) {
						repository = findControlledVocabularyRepository( vocabURI );
					}
				}
				catch ( URISyntaxException e )
				{
					LOGGER.warn( "Controlled Vocabulary Repository URI is invalid: {}", e.getMessage());
				}

				node = new ControlledVocabularyNode( locationPath,
					mapNodeToText( domNode ),
					document.getLocationInfo( domNode ).orElse( null ),
					repository );
			}
			else
			{
				node = new NodeImpl( locationPath, domNode.getTextContent(), document.getLocationInfo( domNode )
						.orElse( null ) );
			}
			countChildNodes( node, domNode );
			nodes.add( node );
		}
		return nodes;
	}

	private String mapNodeToText( org.w3c.dom.Node domNode )
	{
		if ( domNode.getChildNodes().getLength() == 0 )
		{
			return domNode.getTextContent().trim();
		}
		else
		{
			return domNode.getFirstChild().getTextContent().trim();
		}
	}

	private void countChildNodes( NodeImpl node, org.w3c.dom.Node domNode )
	{
		if ( domNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE )
		{
			NamedNodeMap domNodeAttributes = domNode.getAttributes();
			for ( int i = 0; i < domNodeAttributes.getLength(); i++ )
			{
				node.incrementChildCount( "./@" + domNodeAttributes.item( i ).getNodeName() );
			}

			NodeList childNodes = domNode.getChildNodes();
			for ( int i = 0; i < childNodes.getLength(); i++ )
			{
				node.incrementChildCount( "./" + childNodes.item( i ).getNodeName() );
			}
		}
	}

	private URI getVocabURI( Element node ) throws URISyntaxException
	{
		org.w3c.dom.Attr result = null;

		if ( "anlyUnit".equals( node.getLocalName() ) )
		{
			NodeList childNodeList = node.getElementsByTagName( "concept" );
			if (childNodeList.getLength() > 0)
			{
				Element conceptElement = (Element) childNodeList.item( 0 );
				result = conceptElement.getAttributeNode( "vocabURI" );
			}
		}
		else
		{
			// Concept element
			result = node.getAttributeNode( "vocabURI" );
		}

		return result != null ? new URI( result.getValue() ) : null;
	}

	@Override
	public void register( ControlledVocabularyRepository controlledVocabularyRepository )
	{
		requireNonNull( controlledVocabularyRepository, "controlledVocabularyRepository must not be null" );
		controlledVocabularyRepositoryMap.put( controlledVocabularyRepository.getUri(), controlledVocabularyRepository );
	}

	@Override
	public ControlledVocabularyRepository findControlledVocabularyRepository( URI uri )
	{
		requireNonNull( uri, "uri must not be null" );
		return controlledVocabularyRepositoryMap.get( uri );
	}

	@Override
	public String toString()
	{
		return "DomCodebookDocument{uri=" + uri + '}';
	}
}
