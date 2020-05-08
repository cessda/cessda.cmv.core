package eu.cessda.cmv.core.mediatype.profile.v0.xml;

import org.junit.jupiter.api.Test;

import eu.cessda.cmv.core.MaximumElementOccuranceConstraint;

public class JaxbCmvProfileV0Test
{
	@Test
	public void test()
	{
		JaxbCmvProfileV0 profile = new JaxbCmvProfileV0();
		profile.setName( "cdc" );
		JaxbConstraintV0 constraint = new JaxbConstraintV0();
		constraint.setType( "eu.cessda.cmv.core.MaximumElementOccuranceConstraint" );
		profile.getConstraints().add( constraint );
		JaxbConstraintPropertyV0 constraintProperty = new JaxbConstraintPropertyV0();
		constraintProperty = new JaxbConstraintPropertyV0();
		constraintProperty.setName( "locationPath" );
		constraintProperty.setValue( "/path/to/element" );
		constraint.getConstraintProperties().add( constraintProperty );
		constraintProperty = new JaxbConstraintPropertyV0();
		constraintProperty.setName( "maxOccurs" );
		constraintProperty.setValue( "3" );
		constraint.getConstraintProperties().add( constraintProperty );

		System.out.println( profile.toString() );
		System.out.println( MaximumElementOccuranceConstraint.PROPERTIES );
	}
}
