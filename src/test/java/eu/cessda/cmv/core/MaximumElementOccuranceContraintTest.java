package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.Factory.newDocument;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.junit.jupiter.api.Test;

public class MaximumElementOccuranceContraintTest
{
	private TestEnv.V13 testEnv = DefaultTestEnv.newInstance( MaximumElementOccuranceContraintTest.class );

	@Test
	public void validate_invalid() throws Exception
	{
		// given
		Document document = newDocument( testEnv.findTestResourceByName( "bad-case.xml" ) );
		Profile profile = Factory.newProfile( testEnv.findTestResourceByName( "profile.xml" ).toURI().toURL() );

		// when
		ValidationGate.V10 validationGate = new ExtendedValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(), containsString( "exceeds maximal count" ) );
	}

	@Test
	public void validate_valid() throws Exception
	{
		// given
		Document document = newDocument( testEnv.findTestResourceByName( "good-case.xml" ) );
		Profile profile = Factory.newProfile( testEnv.findTestResourceByName( "profile.xml" ).toURI().toURL() );

		// when
		ValidationGate.V10 validationGate = new ExtendedValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 0 ) );
	}
}
