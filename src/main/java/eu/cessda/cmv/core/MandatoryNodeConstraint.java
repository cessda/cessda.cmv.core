package eu.cessda.cmv.core;

import static java.util.Arrays.asList;

import java.util.List;

class MandatoryNodeConstraint extends NodeConstraint
{
	MandatoryNodeConstraint( String locationPath )
	{
		super( locationPath );
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document document )
	{
		long count = ((Document.V10) document).getNodes( getLocationPath() ).size();
		return asList( (T) new MandatoryNodeValidator( getLocationPath(), count ) );
	}
}
