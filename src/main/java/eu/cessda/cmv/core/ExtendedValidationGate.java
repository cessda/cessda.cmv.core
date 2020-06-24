package eu.cessda.cmv.core;

class ExtendedValidationGate extends StandardValidationGate
{
	public ExtendedValidationGate()
	{
		addConstraintType( MaximumElementOccuranceConstraint.class.getCanonicalName() );
	}
}
