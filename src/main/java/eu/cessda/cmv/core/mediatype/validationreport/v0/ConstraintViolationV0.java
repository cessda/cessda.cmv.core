package eu.cessda.cmv.core.mediatype.validationreport.v0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import eu.cessda.cmv.core.ConstraintViolation;

@XmlType( name = ValidationReportV0.CONSTRAINTVIOLATION_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class ConstraintViolationV0
{
	@XmlElement( required = true )
	private String message;

	@XmlElement( name = ValidationReportV0.LOCATIONINFO_ELEMENT )
	private LocationInfoV0 locationInfo;

	public ConstraintViolationV0()
	{
		locationInfo = null;
	}

	public ConstraintViolationV0( ConstraintViolation constraintViolation )
	{
		this();
		message = constraintViolation.getMessage();
		constraintViolation.getLocationInfo().ifPresent( li -> locationInfo = new LocationInfoV0( li ) );
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage( String message )
	{
		this.message = message;
	}

	public LocationInfoV0 getLocationInfo()
	{
		return locationInfo;
	}

	public void setLocationInfo( LocationInfoV0 locationInfo )
	{
		this.locationInfo = locationInfo;
	}
}
