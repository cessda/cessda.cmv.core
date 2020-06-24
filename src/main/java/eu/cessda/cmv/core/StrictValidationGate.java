package eu.cessda.cmv.core;

class StrictValidationGate extends ExtendedValidationGate
{
	public StrictValidationGate()
	{
		addConstraintType( OptionalNodeConstraint.class.getCanonicalName() );
	}
}
