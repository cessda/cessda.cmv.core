package eu.cessda.cmv.core;

class BasicPlusValidationGate extends BasicValidationGate
{
	/**
	 * @deprecated Use
	 *             {@link eu.cessda.cmv.core.CessdaMetadataValidatorFactory#newValidationGate(ValidationGateName)}
	 *             instead
	 */
	@Deprecated
	public BasicPlusValidationGate()
	{
		addConstraintType( CodeValueOfControlledVocabularyConstraint.class.getCanonicalName() );
		addConstraintType( ControlledVocabularyRepositoryConstraint.class.getCanonicalName() );
		addConstraintType( DescriptiveTermOfControlledVocabularyConstraint.class.getCanonicalName() );
	}
}
