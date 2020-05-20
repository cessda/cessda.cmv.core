package eu.cessda.cmv.core;

import static org.gesis.commons.test.hamcrest.OptionalMatchers.isEmpty;
import static org.gesis.commons.test.hamcrest.OptionalMatchers.isPresent;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public class MinimumElementOccuranceValidatorTest
{
	@Test
	public void validateValid()
	{
		// given
		long actualOccurs = 1;
		long minOccurs = 1;
		Validator.V10 validator = new MinimumElementOccuranceValidator( "/path/to/element", actualOccurs, minOccurs );

		// when
		Optional<ConstraintViolation> constraintViolation = validator.validate();

		// then
		assertThat( constraintViolation, isEmpty() );
	}

	@Test
	public void validateInvalid()
	{
		// given
		long actualOccurs = 7;
		long minOccurs = 8;
		Validator.V10 validator = new MinimumElementOccuranceValidator( "/path/to/element", actualOccurs, minOccurs );

		// when
		Optional<ConstraintViolation> constraintViolation = validator.validate();

		// then
		assertThat( constraintViolation, isPresent() );
		assertThat( constraintViolation.get().getMessage(), containsString( "deceeds minimal count" ) );
	}

	@Test
	public void construct()
	{
		assertThrows( NullPointerException.class, () -> new MinimumElementOccuranceValidator( null, 1, 1 ) );
		assertThrows( IllegalArgumentException.class,
				() -> new MaximumElementOccuranceValidator( "/path/to/element", -1, 1 ) );
		assertThrows( IllegalArgumentException.class,
				() -> new MaximumElementOccuranceValidator( "/path/to/element", 1, -1 ) );
	}
}
