package eu.cessda.cmv.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.gesis.commons.xml.SaxXercesAgainstSchemaValidator;
import org.gesis.commons.xml.ddi.Ddi251ClasspathEntityResolver;
import org.junit.jupiter.api.Test;

public class OptionalNodeConstraintTest
{
	private TestEnv.V14 testEnv;
	private CessdaMetadataValidatorFactory factory;
	private ValidationGate.V10 validationGate;

	public OptionalNodeConstraintTest()
	{
		testEnv = DefaultTestEnv.newInstance( OptionalNodeConstraintTest.class );
		SaxXercesAgainstSchemaValidator xmlValidator = new SaxXercesAgainstSchemaValidator();
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/12-document-invalid-blank.xml" ),
				new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/12-document-invalid-missing.xml" ),
				new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/12-document-valid.xml" ),
				new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/12-profile.xml" ) );
		factory = new CessdaMetadataValidatorFactory();
		validationGate = new StrictValidationGate();
	}

	@Test
	public void validate_valid()
	{
		// given
		Profile profile = factory.newProfile( testEnv.findTestResourceByName( "ddi-v25/12-profile.xml" ) );
		Document document = factory.newDocument( testEnv.findTestResourceByName( "ddi-v25/12-document-valid.xml" ) );

		// when
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 0 ) );
	}

	@Test
	public void validate_invalid_missing()
	{
		// given
		Profile profile = factory.newProfile( testEnv.findTestResourceByName( "12-profile.xml" ) );
		Document document = factory.newDocument( testEnv.findTestResourceByName( "12-document-invalid-missing.xml" ) );

		// when
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(), containsString( "is optional" ) );
	}

	@Test
	public void validate_invalid_blank()
	{
		// given
		Profile profile = factory.newProfile( testEnv.findTestResourceByName( "12-profile.xml" ) );
		Document document = factory.newDocument( testEnv.findTestResourceByName( "12-document-invalid-blank.xml" ) );

		// when
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(), containsString( "is blank" ) );
	}
}
