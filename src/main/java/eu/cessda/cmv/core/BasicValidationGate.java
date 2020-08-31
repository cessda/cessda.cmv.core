package eu.cessda.cmv.core;

class BasicValidationGate extends AbstractValidationGate
{
	public BasicValidationGate()
	{
		addConstraintType( MandatoryNodeConstraint.class.getCanonicalName() );
		addConstraintType( MandatoryNodeIfParentPresentConstraint.class.getCanonicalName() );
	}
}
