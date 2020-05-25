package eu.cessda.cmv.core.mediatype.profile.v0;

public class CompilableXPathConstraintV0 extends NodeConstraint
{
	public static final String JAXB_ELEMENT = "CompilableXPathConstraint";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	public CompilableXPathConstraintV0()
	{
		super( null );
	}

	public CompilableXPathConstraintV0( String locationPath )
	{
		super( locationPath );
	}
}
