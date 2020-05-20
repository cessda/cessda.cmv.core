package eu.cessda.cmv.core;

public class ProfileValidationGate extends BasicValidationGate
{
	public ProfileValidationGate()
	{
		addConstraintType( PredicatelessXPathConstraint.class.getCanonicalName() );
		addConstraintType( CompilableXPathConstraint.class.getCanonicalName() );
	}
}
