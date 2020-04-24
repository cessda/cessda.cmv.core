package eu.cessda.cmv.core;

public class ProfileValidationGate extends BasicValidationGate
{
	public ProfileValidationGate()
	{
		getConstraintTypes().add( PredicatelessXPathConstraint.class );
		getConstraintTypes().add( CompilableXPathConstraint.class );
	}
}
