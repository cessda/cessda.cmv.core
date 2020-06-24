package eu.cessda.cmv.core;

class StrictValidationGate extends ExtendedValidationGate
{
	public StrictValidationGate()
	{
		addConstraintType( MaximumElementOccuranceConstraint.class.getCanonicalName() );
		addConstraintType( NodeInProfileConstraint.class.getCanonicalName() );
	}
}
