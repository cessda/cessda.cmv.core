package eu.cessda.cmv.core.mediatype.profile.v0.xml;

import org.junit.jupiter.api.Test;

public class JaxbCmvProfileV0Test
{
	@Test
	public void test()
	{
		JaxbCmvProfileV0 profile = new JaxbCmvProfileV0();
		profile.setName( "cdc" );

		JaxbMaximumElementOccuranceConstraintV0 mecc = new JaxbMaximumElementOccuranceConstraintV0();
		mecc.setLocationPath( "/path/to/element" );
		mecc.setMaxOccurs( 3 );
		profile.getConstraints().add( mecc );

		profile.getConstraints().add( new JaxbMandatoryNodeConstraintV0( "/path/to/element" ) );
		profile.getConstraints().add( new JaxbOptionalNodeConstraintV0( "/path/to/element" ) );
		System.out.println( profile.toString() );
	}
}
