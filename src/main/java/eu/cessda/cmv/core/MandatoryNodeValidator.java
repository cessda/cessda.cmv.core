package eu.cessda.cmv.core;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

class MandatoryNodeValidator implements Validator.V10
{
	private String locationPath;
	private long count;

	MandatoryNodeValidator( String locationPath, long count )
	{
		this.locationPath = locationPath;
		this.count = count;
	}

	@Override
	@SuppressWarnings( "unchecked" )
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
		String message = "'%s' is mandatory";
		message = String.format( message, locationPath );
		return new ConstraintViolation( message, 0, 0 );
	}
}
