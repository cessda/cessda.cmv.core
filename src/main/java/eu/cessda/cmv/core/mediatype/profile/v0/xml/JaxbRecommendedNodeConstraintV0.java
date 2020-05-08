package eu.cessda.cmv.core.mediatype.profile.v0.xml;

public class JaxbRecommendedNodeConstraintV0 extends JaxbNodeConstraint
{
	public static final String JAXB_ELEMENT = "RecommendedNodeConstraint";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	public JaxbRecommendedNodeConstraintV0()
	{
		super( null );
	}

	public JaxbRecommendedNodeConstraintV0( String locationPath )
	{
		super( locationPath );
	}
}
