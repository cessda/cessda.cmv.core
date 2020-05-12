package eu.cessda.cmv.core;

import java.util.Optional;

class OptionalNodeValidator extends MinimumElementOccuranceValidator
{
	private String locationPath;

	OptionalNodeValidator( String locationPath, long actualCount )
	{
		super( locationPath, actualCount, 1 );
	}

	@Override
	protected ConstraintViolation newConstraintViolation()
	{
		String message = "'%s' is optional";
		message = String.format( message, locationPath );
		return new ConstraintViolation( message, Optional.empty() );
	}
}
