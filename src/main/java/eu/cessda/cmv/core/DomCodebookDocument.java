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
import org.gesis.commons.xml.XercesXalanDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Objects.requireNonNull;

class DomCodebookDocument implements Document
{
	private static final Logger LOGGER = LoggerFactory.getLogger( DomCodebookDocument.class );

	private final org.gesis.commons.xml.DomDocument.V12 document;
	private final HashMap<String, ControlledVocabularyRepository> controlledVocabularyRepositoryMap = new HashMap<>();

	public DomCodebookDocument( InputStream inputStream )
	{
		requireNonNull( inputStream );

		document = XercesXalanDocument.newBuilder()
				.ofInputStream( inputStream )
				.locationInfoAware()
				.namespaceAware()
				.build();
	}

	@Override
	public List<Node> getNodes( String locationPath )
	{
		requireNonNull( locationPath );

		List<Node> nodes = new ArrayList<>();
		for ( org.w3c.dom.Node domNode : document.selectNodes( locationPath ) )
		{
			Node node;
			if ( locationPath.equals( "/codeBook/stdyDscr/stdyInfo/sumDscr/anlyUnit" )
					|| locationPath.equals( "/codeBook/stdyDscr/stdyInfo/sumDscr/anlyUnit/concept" ) )
			{
				String vocabURI = getVocabURI( domNode );

				ControlledVocabularyRepository repository = null;
				if (vocabURI != null) {
					repository = findControlledVocabularyRepository( vocabURI );
				}

				node = new ControlledVocabularyNode( locationPath,
					mapNodeToText( domNode ),
					document.getLocationInfo( domNode ).orElse( null ),
					repository );
			}
			else
			{
				node = new Node( locationPath, domNode.getTextContent(), document.getLocationInfo( domNode )
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

	private void countChildNodes( Node node, org.w3c.dom.Node domNode )
	{
		if ( domNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE )
		{
			for ( int i = 0; i < domNode.getAttributes().getLength(); i++ )
			{
				node.incrementChildCount( "./@" + domNode.getAttributes().item( i ).getNodeName() );
			}
			for ( int i = 0; i < domNode.getChildNodes().getLength(); i++ )
			{
				node.incrementChildCount( "./" + domNode.getChildNodes().item( i ).getNodeName() );
			}
		}
	}

	private String getVocabURI( org.w3c.dom.Node node )
	{
		org.w3c.dom.Node result = null;
		if ( node.getNodeName().equals( "concept" ) )
		{
			if ( node.getAttributes() != null )
			{
				result = node.getAttributes().getNamedItem( "vocabURI" );
			}

		}
		else if ( node.getNodeName().equals( "anlyUnit" ) )
		{
			result = document.selectNode( node, "concept/@vocabURI" );
		}

		return result != null ? result.getTextContent() : null;
	}

	@Override
	public void register( String uri, ControlledVocabularyRepository controlledVocabularyRepository )
	{
		requireNonNull( uri, "uri must not be null" );
		requireNonNull( controlledVocabularyRepository, "controlledVocabularyRepository must not be null" );
		controlledVocabularyRepositoryMap.put( uri, controlledVocabularyRepository );
	}

	@Override
	public ControlledVocabularyRepository findControlledVocabularyRepository( String uri )
	{
		requireNonNull( uri, "uri must not be null" );
		if ( controlledVocabularyRepositoryMap.containsKey( uri ) )
		{
			return controlledVocabularyRepositoryMap.get( uri );
		}
		else
		{
			// Log that the repository was not found, and cache this result so that logs are only output once
			LOGGER.debug( "ControlledVocabularyRepository for '{}' not found", uri );
			controlledVocabularyRepositoryMap.put( uri, null );
			return null;
		}
	}
}
