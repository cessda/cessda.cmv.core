package eu.cessda.cmv.core;

public class StrictValidationGate extends ExtendedValidationGate
{
	public StrictValidationGate()
	{
		getConstraintTypes().add( OptionalNodeConstraint.class );
	}
}
