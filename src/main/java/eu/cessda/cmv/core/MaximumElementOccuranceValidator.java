package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

class MaximumElementOccuranceValidator implements Validator.V10
{
	private String locationPath;
	private long actualCount;
	private long maximalCount;

	MaximumElementOccuranceValidator( String locationPath, long actualCount, long maximalCount )
	{
		requireNonNull( locationPath );
		requireNonNegativeLong( actualCount );
		requireNonNegativeLong( maximalCount );

		this.locationPath = locationPath;
		this.actualCount = actualCount;
		this.maximalCount = maximalCount;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		if ( actualCount > maximalCount )
		{
			return of( newConstraintViolation() );
		}
		else
		{
			return empty();
		}
	}

	private static void requireNonNegativeLong( long value )
	{
		if ( value < 0 )
		{
			throw new IllegalArgumentException( "Parameter is negative" );
		}
	}

	private ConstraintViolation newConstraintViolation()
	{
		String message = "'%s' exceeds maximal count of %s";
		message = String.format( message, locationPath, maximalCount );
		return new ConstraintViolation( message, Optional.empty() );
	}
}
