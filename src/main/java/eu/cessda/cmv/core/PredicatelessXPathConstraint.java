package eu.cessda.cmv.core;

import java.util.List;
import java.util.stream.Collectors;

class PredicatelessXPathConstraint extends NodeConstraint
{
	PredicatelessXPathConstraint( String locationPath )
	{
		super( locationPath );
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document document )
	{
		return (List<T>) ((Document.V10) document).getNodes( getLocationPath() ).stream()
				.map( node -> new PredicatelessXPathValidator( node ) )
				.collect( Collectors.toList() );
	}

}

// implements Constraint.V20{
//
// @Override
// @SuppressWarnings( "unchecked" )
// public <T extends Validator> List<T> newValidators( Document profile )
// {
// return (List<T>) ((Document.V10) profile).getNodes( "/DDIProfile/Used/@xpath" ).stream()
// .map( Node::getTextContent )
// .map( PredicatelessXPathValidator::new )
// .collect( Collectors.toList() );
// }
// }
