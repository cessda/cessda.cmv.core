package eu.cessda.cmv.core;

import java.util.Optional;

class MandatoryNodeValidator extends MinimumElementOccuranceValidator
{
	private String locationPath;

	MandatoryNodeValidator( String locationPath, long actualCount )
	{
		super( locationPath, actualCount, 1 );
	}

	@Override
	protected ConstraintViolation newConstraintViolation()
	{
		String message = "'%s' is mandatory";
		message = String.format( message, locationPath );
		return new ConstraintViolation( message, Optional.empty() );
	}
}
