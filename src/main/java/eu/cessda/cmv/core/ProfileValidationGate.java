package eu.cessda.cmv.core;

class ProfileValidationGate extends BasicValidationGate
{
	public ProfileValidationGate()
	{
		addConstraintType( PredicatelessXPathConstraint.class.getCanonicalName() );
		addConstraintType( CompilableXPathConstraint.class.getCanonicalName() );
	}
}
