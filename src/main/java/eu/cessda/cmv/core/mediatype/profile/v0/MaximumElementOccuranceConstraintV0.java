package eu.cessda.cmv.core.mediatype.profile.v0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType( name = MaximumElementOccuranceConstraintV0.JAXB_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class MaximumElementOccuranceConstraintV0 extends NodeConstraint
{
	public static final String JAXB_ELEMENT = "MaximumElementOccuranceConstraint";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	@XmlElement
	private long maxOccurs;

	public MaximumElementOccuranceConstraintV0()
	{
		super( null );
	}

	public MaximumElementOccuranceConstraintV0( String locationPath, long maxOccurs )
	{
		super( locationPath );
		this.maxOccurs = maxOccurs;
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
