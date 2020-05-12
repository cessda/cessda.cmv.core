package eu.cessda.cmv.core;

import java.util.Optional;

class RecommendedNodeValidator extends MinimumElementOccuranceValidator
{
	public RecommendedNodeValidator( String locationPath, long actualCount )
	{
		super( locationPath, actualCount, 1 );
	}

	@Override
	protected ConstraintViolation newConstraintViolation()
	{
		String message = "'%s' is recommended";
		message = String.format( message, getLocationPath() );
		return new ConstraintViolation( message, Optional.empty() );
	}
}
