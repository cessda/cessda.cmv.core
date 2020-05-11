package eu.cessda.cmv.core.mediatype.profile.v0.xml;

public class JaxbOptionalNodeConstraintV0 extends JaxbNodeConstraint
{
	public static final String JAXB_ELEMENT = "OptionalNodeConstraint";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	public JaxbOptionalNodeConstraintV0()
	{
		super( null );
	}

	public JaxbOptionalNodeConstraintV0( String locationPath )
	{
		super( locationPath );
	}
}
