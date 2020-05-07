package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

class MaximumElementOccuranceValidator implements Validator.V10
{
	private String locationPath;
	private long actualOccurs;
	private long maxOccurs;

	MaximumElementOccuranceValidator( String locationPath, long actualCount, long maxOccurs )
	{
		requireNonNull( locationPath );
		requireNonNegativeLong( actualCount );
		requireNonNegativeLong( maxOccurs );

		this.locationPath = locationPath;
		this.actualOccurs = actualCount;
		this.maxOccurs = maxOccurs;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		if ( actualOccurs > maxOccurs )
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
		message = String.format( message, locationPath, maxOccurs );
		return new ConstraintViolation( message, Optional.empty() );
	}
}
