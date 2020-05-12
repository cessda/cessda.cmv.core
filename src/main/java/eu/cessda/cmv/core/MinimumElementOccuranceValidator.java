package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

class MinimumElementOccuranceValidator implements Validator.V10
{
	private String locationPath;
	private long actualOccurs;
	private long minOccurs;

	MinimumElementOccuranceValidator( String locationPath, long actualCount, long minOccurs )
	{
		requireNonNull( locationPath );
		requireNonNegativeLong( actualCount );
		requireNonNegativeLong( minOccurs );

		this.locationPath = locationPath;
		this.actualOccurs = actualCount;
		this.minOccurs = minOccurs;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		if ( actualOccurs < minOccurs )
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

	protected String getLocationPath()
	{
		return locationPath;
	}

	protected ConstraintViolation newConstraintViolation()
	{
		String message = "'%s' deceeds minimal count of %s";
		message = String.format( message, locationPath, minOccurs );
		return new ConstraintViolation( message, Optional.empty() );
	}
}
