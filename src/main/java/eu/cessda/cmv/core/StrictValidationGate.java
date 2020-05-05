package eu.cessda.cmv.core;

public class StrictValidationGate extends StandardValidationGate
{
	public StrictValidationGate()
	{
		getConstraintTypes().add( OptionalNodeConstraint.class );
	}
}
