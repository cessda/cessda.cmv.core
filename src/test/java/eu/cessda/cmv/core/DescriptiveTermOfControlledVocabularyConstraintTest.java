package eu.cessda.cmv.core;

import static java.lang.Long.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.junit.jupiter.api.Test;

class DescriptiveTermOfControlledVocabularyConstraintTest
{
	private TestEnv.V13 testEnv;
	private CessdaMetadataValidatorFactory factory;

	DescriptiveTermOfControlledVocabularyConstraintTest()
	{
		testEnv = DefaultTestEnv.newInstance( DescriptiveTermOfControlledVocabularyConstraintTest.class );
		factory = new CessdaMetadataValidatorFactory();
	}

	@Test
	void validate_valid()
	{
		// given
		Document.V11 document = factory.newDocument( testEnv.findTestResourceByName( "27-document-valid-1.xml" ) );
		Profile.V10 profile = factory.newProfile( testEnv.findTestResourceByName( "27-profile.xml" ) );

		// when
		ValidationGate.V10 validationGate = new StandardValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( profile.getConstraints().stream()
				.filter( cv -> cv instanceof ControlledVocabularyRepositoryConstraint )
				.count(), is( valueOf( 4 ) ) );
		assertThat( profile.getConstraints().stream()
				.filter( cv -> cv instanceof DescriptiveTermOfControlledVocabularyConstraint )
				.count(), is( valueOf( 1 ) ) );
		assertThat( constraintViolations, hasSize( 0 ) );
	}

	@Test
	void validate_invalid_not_element()
	{
		// given
		Document.V11 document = factory.newDocument( testEnv.findTestResourceByName( "27-document-invalid-1.xml" ) );
		Profile.V10 profile = factory.newProfile( testEnv.findTestResourceByName( "27-profile.xml" ) );

		// when
		ValidationGate.V10 validationGate = new StandardValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( profile.getConstraints().stream()
				.filter( cv -> cv instanceof ControlledVocabularyRepositoryConstraint )
				.count(), is( valueOf( 4 ) ) );
		assertThat( profile.getConstraints().stream()
				.filter( cv -> cv instanceof DescriptiveTermOfControlledVocabularyConstraint )
				.count(), is( valueOf( 1 ) ) );
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(),
				containsString( "is not element of the controlled vocabulary" ) );
	}

	@Test
	void validate_invalid_missing_url()
	{
		// given
		Document.V11 document = factory.newDocument( testEnv.findTestResourceByName( "27-document-invalid-2.xml" ) );
		Profile.V10 profile = factory.newProfile( testEnv.findTestResourceByName( "27-profile.xml" ) );

		// when
		ValidationGate.V10 validationGate = new StandardValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(),
				containsString( "is not validateable because no controlled vocabulary is declared" ) );
	}
}
