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
				.map( PredicatelessXPathValidator::new )
				.collect( Collectors.toList() );
	}
}
