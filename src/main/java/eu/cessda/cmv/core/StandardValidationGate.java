package eu.cessda.cmv.core;

class StandardValidationGate extends BasicPlusValidationGate
{
	public StandardValidationGate()
	{
		addConstraintType( RecommendedNodeConstraint.class.getCanonicalName() );
	}
}
