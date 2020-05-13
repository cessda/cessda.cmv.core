package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.gesis.commons.xml.XercesXalanDocument;
import org.gesis.commons.xml.ddi.DdiInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepository;
import eu.cessda.cmv.core.controlledvocabulary.EmptyControlledVocabularyRepository;

public class DomCodebookDocument implements Document.V11
{
	private static final Logger LOGGER = LoggerFactory.getLogger( DomCodebookDocument.class );

	private org.gesis.commons.xml.DomDocument.V12 document;
	private Map<String, ControlledVocabularyRepository> controlledVocabularyRepositoryMap;

	public DomCodebookDocument( DdiInputStream inputStream )
	{
		requireNonNull( inputStream );

		document = XercesXalanDocument.newBuilder()
				.ofInputStream( inputStream )
				.locationInfoAware()
				.build();
		controlledVocabularyRepositoryMap = new HashMap<>();
	}

	@Override
	public List<Node> getNodes( String locationPath )
	{
		List<Node> nodes = new ArrayList<>();
		for ( org.w3c.dom.Node domNode : document.selectNodes( locationPath ) )
		{
			Optional<org.w3c.dom.Node> vocabUriNode = getVocabURINode( domNode );
			if ( vocabUriNode.isPresent() )
			{
				nodes.add( new CodeValueNode( locationPath,
						domNode.getTextContent(),
						document.getLocationInfo( domNode ),
						findControlledVocabularyRepository( vocabUriNode.get().getNodeValue() ) ) );
			}
			else
			{
				nodes.add( new Node( locationPath, domNode.getTextContent(), document.getLocationInfo( domNode ) ) );
			}
		}
		return nodes;
	}

	private Optional<org.w3c.dom.Node> getVocabURINode( org.w3c.dom.Node node )
	{
		if ( node.getAttributes() != null )
		{
			return ofNullable( node.getAttributes().getNamedItem( "vocabURI" ) );
		}
		else
		{
			return empty();
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
