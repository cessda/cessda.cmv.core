package eu.cessda.cmv.core.mediatype.profile.v0;

public class PredicatelessXPathConstraintV0 extends NodeConstraint
{
	public static final String JAXB_ELEMENT = "PredicatelessXPathConstraint";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	public PredicatelessXPathConstraintV0()
	{
		super( null );
	}

	public PredicatelessXPathConstraintV0( String locationPath )
	{
		super( locationPath );
	}
}
