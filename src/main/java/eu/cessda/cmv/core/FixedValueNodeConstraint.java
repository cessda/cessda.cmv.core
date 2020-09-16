package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;

class FixedValueNodeConstraint extends NodeConstraint
{
	private String fixedValue;

	public FixedValueNodeConstraint( String locationPath, String fixedValue )
	{
		super( locationPath );
		this.fixedValue = requireNonNull( fixedValue );
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document document )
	{
		List<Node> nodes = ((Document.V10) document).getNodes( getLocationPath() );
		return (List<T>) nodes.stream()
				.map( node -> new FixedValueNodeValidator( node, fixedValue ) )
				.collect( Collectors.toList() );
	}
}