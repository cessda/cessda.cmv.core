package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.ValidationGateName.EXTENDED;
import static java.lang.Long.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.gesis.commons.xml.SaxXercesAgainstSchemaValidator;
import org.gesis.commons.xml.ddi.Ddi251ClasspathEntityResolver;
import org.junit.jupiter.api.Test;

class FixedValueNodeConstraintTest
{
	private TestEnv.V13 testEnv;
	private Profile.V10 profile;
	private CessdaMetadataValidatorFactory factory;

	FixedValueNodeConstraintTest()
	{
		testEnv = DefaultTestEnv.newInstance( FixedValueNodeConstraintTest.class );
		SaxXercesAgainstSchemaValidator xmlValidator = new SaxXercesAgainstSchemaValidator();
		// TODO https://git.gesis.org/java-commons/commons-xml/issues/70
		// Avoid pulling schema from remote
		// xmlValidator.validate( testEnv.findTestResourceByName( "13-profile.xml" ),
		// new Ddi32ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "13-document-valid-1.xml" ),
				new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "13-document-invalid-1.xml" ),
				new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "13-document-invalid-2.xml" ),
				new Ddi251ClasspathEntityResolver() );

		factory = new CessdaMetadataValidatorFactory();
		profile = factory.newProfile( testEnv.findTestResourceByName( "13-profile.xml" ) );
		assertThat( profile.getConstraints().stream()
				.filter( FixedValueNodeConstraint.class::isInstance )
				.count(), is( valueOf( 1 ) ) );
	}

	@Test
	void validate_valid()
	{
		// given
		Document.V11 document = factory.newDocument( testEnv.findTestResourceByName( "13-document-valid-1.xml" ) );

		// when
		ValidationGate.V10 validationGate = factory.newValidationGate( EXTENDED );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 0 ) );
	}

	@Test
	void validate_invalid_inequalFixedValue()
	{
		// given
		Document.V11 document = factory.newDocument( testEnv.findTestResourceByName( "13-document-invalid-1.xml" ) );

		// when
		ValidationGate.V10 validationGate = factory.newValidationGate( EXTENDED );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(),
				equalTo( "'DDI Analyseeinheit' is not equal to fixed value 'DDI Analysis Unit' in '/codeBook/stdyDscr/stdyInfo/sumDscr/anlyUnit/concept/@vocab' (lineNumber: 13)" ) );
	}

	@Test
	void validate_invalid_missing()
	{
		// given
		Document.V11 document = factory.newDocument( testEnv.findTestResourceByName( "13-document-invalid-2.xml" ) );

		// when
		ValidationGate.V10 validationGate = factory.newValidationGate( EXTENDED );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(),
				equalTo( "'/codeBook/stdyDscr/stdyInfo/sumDscr/anlyUnit/concept/@vocab' is optional" ) );
	}
}
