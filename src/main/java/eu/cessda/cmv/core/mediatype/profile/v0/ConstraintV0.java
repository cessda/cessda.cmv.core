package eu.cessda.cmv.core.mediatype.profile.v0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType( name = ConstraintV0.JAXB_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public abstract class ConstraintV0
{
	public static final String JAXB_ELEMENT = "Constraint";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	public ConstraintV0()
	{
		// Sonar expects explicit constructor
	}
}
