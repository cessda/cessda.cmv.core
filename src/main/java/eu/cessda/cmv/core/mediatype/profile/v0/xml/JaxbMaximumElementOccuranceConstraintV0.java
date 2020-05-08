package eu.cessda.cmv.core.mediatype.profile.v0.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType( name = JaxbMaximumElementOccuranceConstraintV0.JAXB_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class JaxbMaximumElementOccuranceConstraintV0 extends JaxbNodeConstraint
{
	public static final String JAXB_ELEMENT = "MaximumElementOccuranceConstraint";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	@XmlElement
	private long maxOccurs;

	public JaxbMaximumElementOccuranceConstraintV0()
	{
		super( null );
	}

	protected JaxbMaximumElementOccuranceConstraintV0( String locationPath )
	{
		super( locationPath );
	}

	public long getMaxOccurs()
	{
		return maxOccurs;
	}

	public void setMaxOccurs( long maxOccurs )
	{
		this.maxOccurs = maxOccurs;
	}
}
