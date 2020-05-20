package eu.cessda.cmv.core;

public class BasicValidationGate extends AbstractValidationGate
{
	public BasicValidationGate()
	{
		addConstraintType( MandatoryNodeConstraint.class.getCanonicalName() );
	}
}
