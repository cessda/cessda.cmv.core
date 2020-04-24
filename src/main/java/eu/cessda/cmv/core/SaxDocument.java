package eu.cessda.cmv.core;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.gesis.commons.xml.ddi.DdiInputStream;

public class SaxDocument implements Document.V10
{
	private Map<String, List<Node>> nodeListMap;

	public SaxDocument( DdiInputStream inputStream )
	{
	}

	private void addNode( String locationPath, Node node )
	{
		List<Node> nodes;
		if ( nodeListMap.containsKey( locationPath ) )
		{
			nodes = nodeListMap.get( locationPath );
		}
		else
		{
			nodes = new ArrayList<>();
			nodeListMap.put( locationPath, nodes );
		}
		nodes.add( node );
	}

	@Override
	public List<Node> getNodes( String locationPath )
	{
		if ( nodeListMap.containsKey( locationPath ) )
		{
			return unmodifiableList( nodeListMap.get( locationPath ) );
		}
		else
		{
			return emptyList();
		}
	}
}
