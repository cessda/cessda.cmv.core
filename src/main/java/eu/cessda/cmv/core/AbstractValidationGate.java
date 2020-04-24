package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class AbstractValidationGate implements ValidationGate.V10
{
	private List<Class<?>> constraintTypes;

	public AbstractValidationGate()
	{
		constraintTypes = new ArrayList<>();
	}

	protected List<Class<?>> getConstraintTypes()
	{
		return constraintTypes;
	}

	@Override
	public <T extends ValidationReport> T validate( Document document, Profile profile )
	{
		requireNonNull( document );
		requireNonNull( profile );

		List<ConstraintViolation> constraintViolations = ((Profile.V10) profile).getConstraints().stream()
				.filter( constraint -> constraintTypes.contains( constraint.getClass() ) )
				.map( Constraint.V20.class::cast )
				.map( constraint -> constraint.newValidators( document ) )
				.flatMap( List::stream )
				.map( Validator.V10.class::cast )
				.map( Validator.V10::validate )
				.filter( Optional::isPresent ).map( Optional::get )
				.collect( Collectors.toList() );

		return (T) new JaxbValidationReport( constraintViolations );
	}
}