package eu.cessda.cmv.core.mediatype.profile.v0.xml;

public class JaxbMandatoryNodeConstraintV0 extends JaxbNodeConstraint
{
	public static final String JAXB_ELEMENT = "MandatoryNodeConstraint";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	public JaxbMandatoryNodeConstraintV0()
	{
		super( null );
	}

	public JaxbMandatoryNodeConstraintV0( String locationPath )
	{
		super( locationPath );
	}
}
