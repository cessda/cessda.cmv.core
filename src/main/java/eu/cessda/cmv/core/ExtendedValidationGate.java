package eu.cessda.cmv.core;

public class ExtendedValidationGate extends StandardValidationGate
{
	public ExtendedValidationGate()
	{
		getConstraintTypes().add( MaximumElementOccuranceConstraint.class );
	}
}
