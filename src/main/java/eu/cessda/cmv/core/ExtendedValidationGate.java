package eu.cessda.cmv.core;

public class ExtendedValidationGate extends StandardValidationGate
{
	public ExtendedValidationGate()
	{
		addConstraintType( MaximumElementOccuranceConstraint.class.getCanonicalName() );
	}
}
