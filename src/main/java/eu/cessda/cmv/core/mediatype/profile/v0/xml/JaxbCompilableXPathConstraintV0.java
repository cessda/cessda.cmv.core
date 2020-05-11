package eu.cessda.cmv.core.mediatype.profile.v0.xml;

public class JaxbCompilableXPathConstraintV0 extends JaxbNodeConstraint
{
	public static final String JAXB_ELEMENT = "CompilableXPathConstraint";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	public JaxbCompilableXPathConstraintV0()
	{
		super( null );
	}

	public JaxbCompilableXPathConstraintV0( String locationPath )
	{
		super( locationPath );
	}
}
