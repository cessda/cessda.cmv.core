package eu.cessda.cmv.core;

public class StrictValidationGate extends ExtendedValidationGate
{
	public StrictValidationGate()
	{
		addConstraintType( OptionalNodeConstraint.class.getCanonicalName() );
	}
}
