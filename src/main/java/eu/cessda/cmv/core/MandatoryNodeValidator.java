package eu.cessda.cmv.core;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

import org.gesis.commons.xml.LocationInfo;
import org.mockito.Mockito;

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
		LocationInfo locationInfo = Mockito.mock( LocationInfo.class );
		Mockito.when( locationInfo.getLineNumber() ).thenReturn( 10 );
		Mockito.when( locationInfo.getColumnNumber() ).thenReturn( 20 );
		return new ConstraintViolation( message, Optional.of( locationInfo ) );
	}
}
