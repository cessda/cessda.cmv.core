package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gesis.commons.xml.XercesXalanDocument;
import org.gesis.commons.xml.ddi.DdiInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepository;
import eu.cessda.cmv.core.controlledvocabulary.EmptyControlledVocabularyRepository;

class DomCodebookDocument implements Document.V11
{
	private static final Logger LOGGER = LoggerFactory.getLogger( DomCodebookDocument.class );

	private org.gesis.commons.xml.DomDocument.V12 document;
	private Map<String, ControlledVocabularyRepository> controlledVocabularyRepositoryMap;

	public DomCodebookDocument( DdiInputStream inputStream )
	{
		requireNonNull( inputStream );

		document = XercesXalanDocument.newBuilder()
				.ofInputStream( inputStream )
				// TODO .locationInfoAware()
				.namespaceAware()
				.build();
		controlledVocabularyRepositoryMap = new HashMap<>();
	}

	@Override
	public List<Node> getNodes( String locationPath )
	{
		List<Node> nodes = new ArrayList<>();
		for ( org.w3c.dom.Node domNode : document.selectNodes( locationPath ) )
		{
			Node node = null;
			if ( locationPath.contentEquals( "/codeBook/stdyDscr/stdyInfo/sumDscr/anlyUnit/concept" ) )
			{
				String vocabUri = getVocabURI( domNode );
				if ( vocabUri != null )
				{
					node = new CodeValueNode( locationPath,
							domNode.getFirstChild().getTextContent().trim(),
							document.getLocationInfo( domNode ),
							findControlledVocabularyRepository( vocabUri ) );
				}
			}
			if ( locationPath.contentEquals( "/codeBook/stdyDscr/stdyInfo/sumDscr/anlyUnit" ) )
			{
				String vocabUri = getVocabURI( domNode );
				if ( vocabUri != null )
				{
					node = new DescriptiveTermNode( locationPath,
							domNode.getFirstChild().getTextContent().trim(),
							document.getLocationInfo( domNode ),
							findControlledVocabularyRepository( vocabUri ) );
				}
			}

			if ( node == null )
			{
				node = new Node( locationPath, domNode.getTextContent(), document.getLocationInfo( domNode ) );
			}
			countChildNodes( node, domNode );
			nodes.add( node );
		}
		return nodes;
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
			// result = document.selectNode( node, "@vocabURI" );
			if ( node.getAttributes() != null )
			{
				result = node.getAttributes().getNamedItem( "vocabURI" );
			}

		}
		else if ( node.getNodeName().equals( "anlyUnit" ) )
		{
			result = document.selectNode( node, "concept/@vocabURI" );
		}
		if ( result == null )
		{
			return null;
		}
		else
		{
			return result.getTextContent();
		}
	}

	@Override
	public void register( String uri, ControlledVocabularyRepository controlledVocabularyRepository )
	{
		controlledVocabularyRepositoryMap.put( uri, controlledVocabularyRepository );
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends ControlledVocabularyRepository> T findControlledVocabularyRepository( String uri )
	{
		if ( controlledVocabularyRepositoryMap.containsKey( uri ) )
		{
			return (T) controlledVocabularyRepositoryMap.get( uri );
		}
		else
		{
			LOGGER.warn( "ControlledVocabularyRepository for '{}' not found", uri );
			return (T) new EmptyControlledVocabularyRepository();
		}
	}
}
