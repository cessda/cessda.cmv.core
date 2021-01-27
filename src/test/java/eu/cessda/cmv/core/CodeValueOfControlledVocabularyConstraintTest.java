package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.ValidationGateName.BASICPLUS;
import static java.lang.Long.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.junit.jupiter.api.Test;

class CodeValueOfControlledVocabularyConstraintTest
{
	private TestEnv.V13 testEnv;
	private Profile.V10 profile;
	private CessdaMetadataValidatorFactory factory;

	CodeValueOfControlledVocabularyConstraintTest()
	{
		testEnv = DefaultTestEnv.newInstance( CodeValueOfControlledVocabularyConstraintTest.class );
		factory = new CessdaMetadataValidatorFactory();
		profile = factory.newProfile( testEnv.findTestResourceByName( "9-profile.xml" ) );
		assertThat( profile.getConstraints().stream()
				.filter( ControlledVocabularyRepositoryConstraint.class::isInstance )
				.count(), is( valueOf( 2 ) ) );
		assertThat( profile.getConstraints().stream()
				.filter( CodeValueOfControlledVocabularyConstraint.class::isInstance )
				.count(), is( valueOf( 1 ) ) );
	}

	@Test
	void validate_invalid_not_element()
	{
		// given
		Document document = factory.newDocument( testEnv.findTestResourceByName( "9-document-invalid-1.xml" ) );

		// when
		ValidationGate.V10 validationGate = factory.newValidationGate( BASICPLUS );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(),
				equalTo( "Code value 'Person' in '/codeBook/stdyDscr/stdyInfo/sumDscr/anlyUnit/concept' is not element of the controlled vocabulary in 'https://vocabularies.cessda.eu/v2/vocabularies/AnalysisUnit/2.0?languageVersion=en-2.0' (lineNumber: 16)" ) );
	}

	@Test
	void validate_invalid_missing_url()
	{
		// given
		Document document = factory.newDocument( testEnv.findTestResourceByName( "9-document-invalid-2.xml" ) );

		// when
		ValidationGate.V10 validationGate = factory.newValidationGate( BASICPLUS );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(),
				equalTo( "Code value 'Family.HouseholdFamily' in '/codeBook/stdyDscr/stdyInfo/sumDscr/anlyUnit/concept' is not validateable because no controlled vocabulary is declared (lineNumber: 28)" ) );
	}

	@Test
	void validate_valid()
	{
		// given
		Document document = factory.newDocument( testEnv.findTestResourceByName( "9-document-valid.xml" ) );

		// when
		ValidationGate.V10 validationGate = factory.newValidationGate( BASICPLUS );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 0 ) );
	}
}
