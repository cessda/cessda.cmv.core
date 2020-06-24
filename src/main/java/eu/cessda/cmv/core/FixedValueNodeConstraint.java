package eu.cessda.cmv.core;

import java.util.ArrayList;
import java.util.List;

class FixedValueNodeConstraint extends NodeConstraint
{
	public FixedValueNodeConstraint( String locationPath )
	{
		super( locationPath );
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document document )
	{
		// TODO Implement FixedValueNodeValidator #13
		List<Validator> validators = new ArrayList<>();
		return (List<T>) validators;
	}
}