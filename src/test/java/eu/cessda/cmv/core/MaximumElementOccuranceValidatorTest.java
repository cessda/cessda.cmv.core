package eu.cessda.cmv.core;

import static org.gesis.commons.test.hamcrest.OptionalMatchers.isEmpty;
import static org.gesis.commons.test.hamcrest.OptionalMatchers.isPresent;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class MaximumElementOccuranceValidatorTest
{
	@Test
	public void validateValid()
	{
		// given
		Validator.V10 validator = new MaximumElementOccuranceValidator( "/some/path/to/element", 5, 6 );
		// when
		Optional<ConstraintViolation> constraintViolation = validator.validate();
		// then
		assertThat( constraintViolation, isEmpty() );
	}

	@Test
	public void validateInvalid()
	{
		// given
		Validator.V10 validator = new MaximumElementOccuranceValidator( "/some/path/to/element", 7, 6 );
		// when
		Optional<ConstraintViolation> constraintViolation = validator.validate();
		// then
		assertThat( constraintViolation, isPresent() );
		assertThat( constraintViolation.get().getMessage(), containsString( "exceeds maximal count" ) );
	}

	@Test
	public void construct()
	{
		assertThrows( NullPointerException.class, () -> new MaximumElementOccuranceValidator( null, 1, 1 ) );
		assertThrows( IllegalArgumentException.class,
				() -> new MaximumElementOccuranceValidator( "/some/path/to/element", -1, 1 ) );
		assertThrows( IllegalArgumentException.class,
				() -> new MaximumElementOccuranceValidator( "/some/path/to/element", 1, -1 ) );
	}
}
