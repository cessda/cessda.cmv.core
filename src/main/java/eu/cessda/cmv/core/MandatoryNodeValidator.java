package eu.cessda.cmv.core;

import java.util.Optional;

class MandatoryNodeValidator extends MinimumElementOccuranceValidator
{
	public MandatoryNodeValidator( String locationPath, long actualCount )
	{
		super( locationPath, actualCount, 1 );
	}

	@Override
	protected ConstraintViolation newConstraintViolation()
	{
		String message = "'%s' is mandatory";
		message = String.format( message, getLocationPath() );
		return new ConstraintViolation( message, Optional.empty() );
	}
}
