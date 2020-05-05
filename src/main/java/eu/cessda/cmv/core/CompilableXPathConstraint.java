package eu.cessda.cmv.core;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.xpath.XPathFactory;

class CompilableXPathConstraint extends NodeConstraint
{
	public CompilableXPathConstraint( String locationPath )
	{
		super( locationPath );
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document document )
	{
		XPathFactory factory = XPathFactory.newInstance();
		return (List<T>) ((Document.V10) document).getNodes( getLocationPath() ).stream()
				.map( node -> new CompilableXPathValidator( node, factory ) )
				.collect( Collectors.toList() );
	}
}
