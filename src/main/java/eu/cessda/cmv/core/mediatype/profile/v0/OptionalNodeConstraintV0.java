package eu.cessda.cmv.core.mediatype.profile.v0;

public class OptionalNodeConstraintV0 extends NodeConstraint
{
	public static final String JAXB_ELEMENT = "OptionalNodeConstraint";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	public OptionalNodeConstraintV0()
	{
		super( null );
	}

	public OptionalNodeConstraintV0( String locationPath )
	{
		super( locationPath );
	}
}
