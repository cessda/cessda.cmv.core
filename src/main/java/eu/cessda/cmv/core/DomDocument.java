package eu.cessda.cmv.core;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.gesis.commons.xml.XercesXalanDocument;
import org.gesis.commons.xml.ddi.DdiInputStream;

public class DomDocument implements Document.V10
{
	private org.gesis.commons.xml.DomDocument.V11 document;

	public DomDocument( DdiInputStream inputStream )
	{
		document = XercesXalanDocument.newBuilder()
				.ofInputStream( inputStream )
				.build();
	}

	@Override
	public List<Node> getNodes( String locationPath )
	{
		return unmodifiableList( document.selectNodes( locationPath ).stream()
				.map( domNode -> new Node( domNode.getTextContent() ) )
				.collect( toList() ) );
	}
}
