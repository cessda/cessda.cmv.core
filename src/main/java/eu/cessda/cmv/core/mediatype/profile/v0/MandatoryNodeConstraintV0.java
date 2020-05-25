package eu.cessda.cmv.core.mediatype.profile.v0;

public class MandatoryNodeConstraintV0 extends NodeConstraint
{
	public static final String JAXB_ELEMENT = "MandatoryNodeConstraint";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	public MandatoryNodeConstraintV0()
	{
		super( null );
	}

	public MandatoryNodeConstraintV0( String locationPath )
	{
		super( locationPath );
	}
}
