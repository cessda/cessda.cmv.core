package eu.cessda.cmv.core;

import java.util.ArrayList;
import java.util.List;

class MandatoryNodeIfParentPresentConstraint extends NodeConstraint
{
	public MandatoryNodeIfParentPresentConstraint( String locationPath )
	{
		super( locationPath );
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document document )
	{
		// TODO Implement MandatoryNodeIfParentPresentValidator #22
		List<Validator> validators = new ArrayList<>();
		return (List<T>) validators;
	}
}