package eu.cessda.cmv.core.mediatype.profile.v0.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType( name = JaxbConstraintPropertyV0.JAXB_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class JaxbConstraintPropertyV0
{
	public static final String JAXB_ELEMENT = "ConstraintProperty";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	@XmlAttribute
	private String name;

	@XmlAttribute
	private String value;

	public JaxbConstraintPropertyV0()
	{
	}

	public JaxbConstraintPropertyV0( String name, String value )
	{
		this.name = name;
		this.value = value;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setValue( String value )
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}
}
