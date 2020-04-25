package eu.cessda.cmv.core;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.xpath.XPathFactory;

class CompilableXPathConstraint implements Constraint.V20
{
	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document profile )
	{
		XPathFactory factory = XPathFactory.newInstance();
		return (List<T>) ((Document.V10) profile).getNodes( "/DDIProfile/Used/@xpath" ).stream()
				.map( node -> new CompilableXPathValidator( node, factory ) )
				.collect( Collectors.toList() );
	}
}
