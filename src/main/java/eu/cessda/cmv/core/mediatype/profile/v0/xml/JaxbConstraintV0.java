package eu.cessda.cmv.core.mediatype.profile.v0.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType( name = JaxbConstraintV0.JAXB_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class JaxbConstraintV0
{
	public static final String JAXB_ELEMENT = "Constraint";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	@XmlAttribute
	private String type;

	@XmlElement( name = JaxbConstraintPropertyV0.JAXB_ELEMENT )
	private List<JaxbConstraintPropertyV0> constraintProperties;

	public JaxbConstraintV0()
	{
		constraintProperties = new ArrayList<>();
	}

	public String getType()
	{
		return type;
	}

	public void setType( String type )
	{
		this.type = type;
	}

	public List<JaxbConstraintPropertyV0> getConstraintProperties()
	{
		return constraintProperties;
	}

	public void setConstraintProperties( List<JaxbConstraintPropertyV0> constraintProperties )
	{
		this.constraintProperties = constraintProperties;
	}
}
