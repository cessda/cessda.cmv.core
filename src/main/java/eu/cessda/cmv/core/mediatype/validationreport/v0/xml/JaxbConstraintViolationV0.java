package eu.cessda.cmv.core.mediatype.validationreport.v0.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import eu.cessda.cmv.core.ConstraintViolation;

@XmlType( name = JaxbConstraintViolationV0.JAXB_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class JaxbConstraintViolationV0
{
	static final String JAXB_ELEMENT = "ConstraintViolation";
	static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	@XmlElement( required = true )
	private String message;

	@XmlElement( name = JaxbLocationInfoV0.JAXB_ELEMENT )
	private JaxbLocationInfoV0 locationInfo;

	public JaxbConstraintViolationV0()
	{
	}

	public JaxbConstraintViolationV0( ConstraintViolation constraintViolation )
	{
		message = constraintViolation.getMessage();
		constraintViolation.getLocationInfo().ifPresent( li -> locationInfo = new JaxbLocationInfoV0( li ) );
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage( String message )
	{
		this.message = message;
	}

	public JaxbLocationInfoV0 getLocationInfo()
	{
		return locationInfo;
	}

	public void setLocationInfo( JaxbLocationInfoV0 locationInfo )
	{
		this.locationInfo = locationInfo;
	}
}
