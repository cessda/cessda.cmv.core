package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import org.gesis.commons.xml.LocationInfo;

public class ConstraintViolation
{
	private String message;

	private Optional<LocationInfo> locationInfo;

	ConstraintViolation( String message, Optional<LocationInfo> locationInfo )
	{
		requireNonNull( message );
		requireNonNull( locationInfo );

		this.message = message;
		this.locationInfo = locationInfo;
	}

	public String getMessage()
	{
		if ( locationInfo.isPresent() )
		{
			return message + " (lineNumber: " + locationInfo.get().getLineNumber() + ")";
		}
		else
		{
			return message;
		}
	}

	public Optional<LocationInfo> getLocationInfo()
	{
		return locationInfo;
	}
}
