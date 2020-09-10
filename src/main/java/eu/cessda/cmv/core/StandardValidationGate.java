package eu.cessda.cmv.core;

class StandardValidationGate extends BasicPlusValidationGate
{
	/**
	 * @deprecated Use
	 *             {@link eu.cessda.cmv.core.CessdaMetadataValidatorFactory#newValidationGate(ValidationGateName)}
	 *             instead
	 */
	@Deprecated
	public StandardValidationGate()
	{
		addConstraintType( RecommendedNodeConstraint.class.getCanonicalName() );
	}
}
