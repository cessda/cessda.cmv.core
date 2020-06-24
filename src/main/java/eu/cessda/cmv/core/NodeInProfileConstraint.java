package eu.cessda.cmv.core;

import java.util.ArrayList;
import java.util.List;

class NodeInProfileConstraint extends NodeConstraint
{
	public NodeInProfileConstraint( String locationPath )
	{
		super( locationPath );
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document document )
	{
		// TODO Implement NodeInProfileValidator #59
		List<Validator> validators = new ArrayList<>();
		return (List<T>) validators;
	}
}