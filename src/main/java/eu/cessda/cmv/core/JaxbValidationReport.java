package eu.cessda.cmv.core;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

import java.util.List;

public class JaxbValidationReport implements ValidationReport.V10
{
	private List<ConstraintViolation> constraintViolations;

	public JaxbValidationReport( List<ConstraintViolation> constraintViolations )
	{
		requireNonNull( constraintViolations );
		this.constraintViolations = unmodifiableList( constraintViolations );
	}

	@Override
	public <T extends ConstraintViolation> List<T> getConstraintViolations()
	{
		return (List<T>) constraintViolations;
	}
}
