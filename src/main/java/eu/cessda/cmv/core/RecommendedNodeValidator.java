package eu.cessda.cmv.core;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

class RecommendedNodeValidator implements Validator.V10
{
	private String locationPath;
	private long count;

	RecommendedNodeValidator( String locationPath, long count )
	{
		this.locationPath = locationPath;
		this.count = count;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		if ( count == 0 )
		{
			return of( newConstraintViolation() );
		}
		else
		{
			return empty();
		}
	}

	private ConstraintViolation newConstraintViolation()
	{
		String message = "'%s' is recommended";
		message = String.format( message, locationPath );
		return new ConstraintViolation( message, 0, 0 );
	}
}
