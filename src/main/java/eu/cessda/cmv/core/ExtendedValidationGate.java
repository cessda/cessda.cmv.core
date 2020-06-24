package eu.cessda.cmv.core;

class ExtendedValidationGate extends StandardValidationGate
{
	public ExtendedValidationGate()
	{
		addConstraintType( FixedValueNodeConstraint.class.getCanonicalName() );
		addConstraintType( OptionalNodeConstraint.class.getCanonicalName() );
	}
}
