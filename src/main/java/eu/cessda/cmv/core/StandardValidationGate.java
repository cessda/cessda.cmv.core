package eu.cessda.cmv.core;

class StandardValidationGate extends BasicValidationGate
{
	public StandardValidationGate()
	{
		addConstraintType( RecommendedNodeConstraint.class.getCanonicalName() );
		addConstraintType( CodeValueOfControlledVocabularyConstraint.class.getCanonicalName() );
		addConstraintType( ControlledVocabularyRepositoryConstraint.class.getCanonicalName() );
	}
}
