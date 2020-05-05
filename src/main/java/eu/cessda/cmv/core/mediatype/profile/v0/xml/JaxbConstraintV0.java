package eu.cessda.cmv.core.mediatype.profile.v0.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType( name = JaxbConstraintV0.JAXB_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class JaxbConstraintV0
{
	public static final String JAXB_ELEMENT = "Constraint";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	@XmlAttribute
	private String type;

	public JaxbConstraintV0()
	{
	}

	public JaxbConstraintV0( String type )
	{
		setType( type );
	}

	public String getType()
	{
		return type;
	}

	public void setType( String type )
	{
		this.type = type;
	}
}
