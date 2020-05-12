package eu.cessda.cmv.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class RecommendedNodeConstraint extends NodeConstraint
{
	public RecommendedNodeConstraint( String locationPath )
	{
		super( locationPath );
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document document )
	{
		List<Node> nodes = ((Document.V10) document).getNodes( getLocationPath() );
		List<Validator> validators = new ArrayList<>();
		validators.add( new RecommendedNodeValidator( getLocationPath(), nodes.size() ) );
		validators.addAll( nodes.stream().map( NotBlankNodeValidator::new ).collect( Collectors.toList() ) );
		return (List<T>) validators;
	}
}