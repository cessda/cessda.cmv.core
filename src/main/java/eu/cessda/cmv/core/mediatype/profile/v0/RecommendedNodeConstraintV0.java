package eu.cessda.cmv.core.mediatype.profile.v0;

public class RecommendedNodeConstraintV0 extends NodeConstraint
{
	public static final String JAXB_ELEMENT = "RecommendedNodeConstraint";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	public RecommendedNodeConstraintV0()
	{
		super( null );
	}

	public RecommendedNodeConstraintV0( String locationPath )
	{
		super( locationPath );
	}
}
