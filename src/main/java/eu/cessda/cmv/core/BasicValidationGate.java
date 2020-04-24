package eu.cessda.cmv.core;

public class BasicValidationGate extends AbstractValidationGate
{
	public BasicValidationGate()
	{
		getConstraintTypes().add( MandatoryNodeConstraint.class );
	}
}
