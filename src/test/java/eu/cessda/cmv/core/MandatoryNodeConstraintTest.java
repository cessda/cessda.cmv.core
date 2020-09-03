package eu.cessda.cmv.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

import java.io.File;
import java.util.List;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.gesis.commons.xml.SaxXercesAgainstSchemaValidator;
import org.gesis.commons.xml.ddi.Ddi251ClasspathEntityResolver;
import org.junit.jupiter.api.Test;

class MandatoryNodeConstraintTest
{
	private TestEnv.V13 testEnv;
	private CessdaMetadataValidatorFactory factory;

	MandatoryNodeConstraintTest()
	{
		testEnv = DefaultTestEnv.newInstance( MandatoryNodeConstraintTest.class );
		SaxXercesAgainstSchemaValidator xmlValidator = new SaxXercesAgainstSchemaValidator();
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/10-document-invalid-blank.xml" ),
				new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/10-document-valid.xml" ),
				new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/10-profile.xml" ) );
		factory = new CessdaMetadataValidatorFactory();
	}

	@Test
	void validate_missing()
	{
		// given
		File file = testEnv.findTestResourceByName( "10-document-invalid-missing.xml-invalid" );
		Document document = factory.newDocument( file );
		Profile profile = factory.newProfile( testEnv.findTestResourceByName( "10-profile.xml" ) );

		// when
		ValidationGate.V10 validationGate = new StrictValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(), containsString( "mandatory" ) );
	}

	@Test
	void validate_blank()
	{
		// given
		File file = testEnv.findTestResourceByName( "10-document-invalid-blank.xml" );
		Document document = factory.newDocument( file );
		Profile profile = factory.newProfile( testEnv.findTestResourceByName( "10-profile.xml" ) );

		// when
		ValidationGate.V10 validationGate = new StrictValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(), containsString( "blank" ) );
	}

	@Test
	void validate_valid()
	{
		// given
		File file = testEnv.findTestResourceByName( "10-document-valid.xml" );
		Document document = factory.newDocument( file );
		Profile profile = factory.newProfile( testEnv.findTestResourceByName( "profile.xml" ) );

		// when
		ValidationGate.V10 validationGate = new StrictValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 0 ) );
	}
}
