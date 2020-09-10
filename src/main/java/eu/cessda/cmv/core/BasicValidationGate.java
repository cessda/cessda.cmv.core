package eu.cessda.cmv.core;

class BasicValidationGate extends AbstractValidationGate
{
	/**
	 * @deprecated Use
	 *             {@link eu.cessda.cmv.core.CessdaMetadataValidatorFactory#newValidationGate(ValidationGateName)}
	 *             instead
	 */
	@Deprecated
	public BasicValidationGate()
	{
		addConstraintType( MandatoryNodeConstraint.class.getCanonicalName() );
		addConstraintType( MandatoryNodeIfParentPresentConstraint.class.getCanonicalName() );
	}
}
