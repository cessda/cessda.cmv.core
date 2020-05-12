package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.Factory.newDocument;
import static eu.cessda.cmv.core.Factory.newProfile;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.junit.jupiter.api.Test;

public class MandatoryNodeConstraintTest
{
	private TestEnv.V13 testEnv = DefaultTestEnv.newInstance( MandatoryNodeConstraintTest.class );

	@Test
	public void validate_missing() throws Exception
	{
		// given
		Document document = newDocument( testEnv.findTestResourceByName( "bad-case-missing.xml-invalid" ) );
		Profile profile = newProfile( testEnv.findTestResourceByName( "profile.xml" ).toURI().toURL() );

		// when
		ValidationGate.V10 validationGate = new StrictValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(), containsString( "mandatory" ) );
	}

	@Test
	public void validate_blank() throws Exception
	{
		// given
		Document document = newDocument( testEnv.findTestResourceByName( "bad-case-blank.xml" ) );
		Profile profile = newProfile( testEnv.findTestResourceByName( "profile.xml" ).toURI().toURL() );

		// when
		ValidationGate.V10 validationGate = new StrictValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(), containsString( "blank" ) );
	}

	@Test
	public void validate_valid() throws Exception
	{
		// given
		Document document = newDocument( testEnv.findTestResourceByName( "good-case.xml" ) );
		Profile profile = newProfile( testEnv.findTestResourceByName( "profile.xml" ).toURI().toURL() );

		// when
		ValidationGate.V10 validationGate = new StrictValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 0 ) );
	}
}
