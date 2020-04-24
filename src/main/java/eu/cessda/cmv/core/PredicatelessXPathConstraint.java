package eu.cessda.cmv.core;

import java.util.List;
import java.util.stream.Collectors;

class PredicatelessXPathConstraint implements Constraint.V20
{
	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document profile )
	{
		return (List<T>) ((Document.V10) profile).getNodes( "/DDIProfile/Used/@xpath" ).stream()
				.map( Node::getTextContent )
				.map( PredicatelessXPathValidator::new )
				.collect( Collectors.toList() );
	}
}
