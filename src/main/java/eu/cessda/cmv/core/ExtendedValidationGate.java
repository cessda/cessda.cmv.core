package eu.cessda.cmv.core;

class ExtendedValidationGate extends StandardValidationGate
{
	/**
	 * @deprecated Use
	 *             {@link eu.cessda.cmv.core.CessdaMetadataValidatorFactory#newValidationGate(ValidationGateName)}
	 *             instead
	 */
	@Deprecated
	public ExtendedValidationGate()
	{
		addConstraintType( FixedValueNodeConstraint.class.getCanonicalName() );
		addConstraintType( OptionalNodeConstraint.class.getCanonicalName() );
	}
}
