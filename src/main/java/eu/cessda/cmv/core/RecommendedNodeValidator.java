package eu.cessda.cmv.core;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.gesis.commons.xml.LocationInfo;
import org.mockito.Mockito;

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
		// TODO Replace by real object
		LocationInfo locationInfo = Mockito.mock( LocationInfo.class );
		when( locationInfo.getLineNumber() ).thenReturn( 10 );
		when( locationInfo.getColumnNumber() ).thenReturn( 20 );
		return new ConstraintViolation( message, Optional.of( locationInfo ) );
	}
}
