package eu.cessda.cmv.core;

class StrictValidationGate extends ExtendedValidationGate
{
	/**
	 * @deprecated Use
	 *             {@link eu.cessda.cmv.core.CessdaMetadataValidatorFactory#newValidationGate(ValidationGateName)}
	 *             instead
	 */
	@Deprecated
	public StrictValidationGate()
	{
		addConstraintType( MaximumElementOccuranceConstraint.class.getCanonicalName() );
		addConstraintType( NodeInProfileConstraint.class.getCanonicalName() );
	}
}
