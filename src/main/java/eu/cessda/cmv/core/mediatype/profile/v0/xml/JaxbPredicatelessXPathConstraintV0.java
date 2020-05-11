package eu.cessda.cmv.core.mediatype.profile.v0.xml;

public class JaxbPredicatelessXPathConstraintV0 extends JaxbNodeConstraint
{
	public static final String JAXB_ELEMENT = "PredicatelessXPathConstraint";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	public JaxbPredicatelessXPathConstraintV0()
	{
		super( null );
	}

	public JaxbPredicatelessXPathConstraintV0( String locationPath )
	{
		super( locationPath );
	}
}
