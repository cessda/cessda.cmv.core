package eu.cessda.cmv.core;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.gesis.commons.xml.XercesXalanDocument;
import org.gesis.commons.xml.ddi.DdiInputStream;

public class DomCodebookDocument implements Document.V10
{
	private org.gesis.commons.xml.DomDocument.V12 document;

	public DomCodebookDocument( DdiInputStream inputStream )
	{
		document = XercesXalanDocument.newBuilder()
				.ofInputStream( inputStream )
				.locationInfoAware()
				.build();
	}

	@Override
	public List<Node> getNodes( String locationPath )
	{
		List<Node> nodes = new ArrayList<>();
		for ( org.w3c.dom.Node domNode : document.selectNodes( locationPath ) )
		{
			Optional<org.w3c.dom.Node> domUri = getVocabURINode( domNode );
			if ( domUri.isPresent() )
			{
				nodes.add( new CodeValueNode( locationPath, domNode.getTextContent(),
						document.getLocationInfo( domNode ), domUri.get().getNodeValue() ) );
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
}
