package eu.cessda.cmv.core;

public class StandardValidationGate extends BasicValidationGate
{
	public StandardValidationGate()
	{
		getConstraintTypes().add( RecommendedNodeConstraint.class );
		getConstraintTypes().add( CodeValueOfControlledVocabularyConstraint.class );
		getConstraintTypes().add( ControlledVocabularyRepositoryConstraint.class );
	}
}
