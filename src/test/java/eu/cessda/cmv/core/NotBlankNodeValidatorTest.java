package eu.cessda.cmv.core;

import static java.util.Optional.empty;
import static org.gesis.commons.test.hamcrest.OptionalMatchers.isEmpty;
import static org.gesis.commons.test.hamcrest.OptionalMatchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class NotBlankNodeValidatorTest
{
	@Test
	void validate_empty()
	{
		// given: empty text content
		Node node = new Node( "/path/to/node", "", empty() );
		Validator.V10 validator = new NotBlankNodeValidator( node );

		// when
		Optional<ConstraintViolation> constraintViolation = validator.validate();

		// then
		assertThat( constraintViolation, isPresent() );
	}

	@Test
	void validate_blank()
	{
		// given: blank text content
		Node node = new Node( "/path/to/node", "   ", empty() );
		Validator.V10 validator = new NotBlankNodeValidator( node );

		// when
		Optional<ConstraintViolation> constraintViolation = validator.validate();

		// then
		assertThat( constraintViolation, isPresent() );
	}

	@Test
	void validate_notBlank()
	{
		// given: not blank text content
		Node node = new Node( "/path/to/node", "not blank", empty() );
		Validator.V10 validator = new NotBlankNodeValidator( node );

		// when
		Optional<ConstraintViolation> constraintViolation = validator.validate();

		// then
		assertThat( constraintViolation, isEmpty() );
	}
}
