package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gesis.commons.xml.LocationInfo;
import org.gesis.commons.xml.jaxb.LocationInfoAdapter;

public class ConstraintViolation
{
	@XmlElement( name = "Message" )
	private String message;

	@XmlElement( name = "LocationInfo" )
	@XmlJavaTypeAdapter( LocationInfoAdapter.class )
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
		return message;
	}

	public Optional<LocationInfo> getLocationInfo()
	{
		return locationInfo;
	}
}
