package eu.cessda.cmv.core;

class BasicPlusValidationGate extends BasicValidationGate
{
	public BasicPlusValidationGate()
	{
		addConstraintType( CodeValueOfControlledVocabularyConstraint.class.getCanonicalName() );
		addConstraintType( ControlledVocabularyRepositoryConstraint.class.getCanonicalName() );
		addConstraintType( MandatoryNodeIfParentPresentConstraint.class.getCanonicalName() );
	}
}
