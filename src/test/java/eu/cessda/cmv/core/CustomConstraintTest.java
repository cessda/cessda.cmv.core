package eu.cessda.cmv.core;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomConstraintTest
{
	private final CessdaMetadataValidatorFactory factory = new CessdaMetadataValidatorFactory();

	@Test
	void shouldCreateValidationGateWithSpecifiedConstraints() throws InvalidGateException
	{
		// given
		Document.V11 document = factory.newDocument(
				this.getClass()
						.getResource( "/eu.cessda.cmv.core.FixedValueNodeConstraintTest/13-document-invalid-1.xml" )
		);
		Profile profile = factory.newProfile(
				this.getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" )
		);

		ValidationGate.V10 validationGate = CessdaMetadataValidatorFactory.newValidationGate( Arrays.asList(
				FixedValueNodeConstraint.class.getSimpleName(),
				MandatoryNodeConstraint.class.getSimpleName()
		) );

		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );
		List<String> messages = constraintViolations.stream()
				.map( ConstraintViolation::getMessage )
				.collect( Collectors.toList() );

		// Assert that both the constraints were enforced
		assertThat( messages, hasItem( containsString( "mandatory" ) ) );
		assertThat( messages, hasItem( containsString( "fixed value" ) ) );
	}

	@SuppressWarnings( "unchecked" )
	@Test
	void shouldThrowIfAnInvalidConstraintIsRequested()
	{
		// InvalidGate is not a class, Validator is a class but isn't a constraint
		InvalidGateException invalidGateException = assertThrows( InvalidGateException.class, () ->
				CessdaMetadataValidatorFactory.newValidationGate( Arrays.asList( "InvalidGate", "Validator" ) )
		);

		// Assert that both types of error were thrown
		assertThat(
				Arrays.asList( invalidGateException.getSuppressed() ),
				everyItem( instanceOf( InvalidConstraintException.class ) )
		);
		assertThat(
				Arrays.stream( invalidGateException.getSuppressed() )
						.map( Throwable::getCause )
						.collect( Collectors.toList() ),
				hasItems( instanceOf( ClassNotFoundException.class ), instanceOf( ClassCastException.class ) )
		);
	}
}
