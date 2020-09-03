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

class CodeValueOfControlledVocabularyConstraintTest
{
	private TestEnv.V13 testEnv;
	private CessdaMetadataValidatorFactory factory;

	CodeValueOfControlledVocabularyConstraintTest()
	{
		testEnv = DefaultTestEnv.newInstance( CodeValueOfControlledVocabularyConstraintTest.class );
		factory = new CessdaMetadataValidatorFactory();
	}

	@Test
	void validate_invalid() throws Exception
	{
		// given
		Document document = factory.newDocument( testEnv.findTestResourceByName( "bad-case.xml" ) );
		Profile.V10 profile = factory.newProfile( testEnv.findTestResourceByName( "profile.xml" ) );

		// when
		ValidationGate.V10 validationGate = new StandardValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( profile.getConstraints().stream()
				.filter( cv -> cv instanceof ControlledVocabularyRepositoryConstraint )
				.count(), is( valueOf( 2 ) ) );
		assertThat( profile.getConstraints().stream()
				.filter( cv -> cv instanceof CodeValueOfControlledVocabularyConstraint )
				.count(), is( valueOf( 1 ) ) );
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(),
				containsString( "is not element of the controlled vocabulary in" ) );
	}

	@Test
	void validate_valid() throws Exception
	{
		// given
		Document document = factory.newDocument( testEnv.findTestResourceByName( "good-case.xml" ) );
		Profile.V10 profile = factory.newProfile( testEnv.findTestResourceByName( "profile.xml" ).toURI().toURL() );

		// when
		ValidationGate.V10 validationGate = new StandardValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( profile.getConstraints().stream()
				.filter( cv -> cv instanceof ControlledVocabularyRepositoryConstraint )
				.count(), is( valueOf( 2 ) ) );
		assertThat( profile.getConstraints().stream()
				.filter( cv -> cv instanceof CodeValueOfControlledVocabularyConstraint )
				.count(), is( valueOf( 1 ) ) );
		assertThat( constraintViolations, hasSize( 0 ) );
	}
}
