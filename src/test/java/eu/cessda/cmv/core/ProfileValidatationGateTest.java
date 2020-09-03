package eu.cessda.cmv.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;

class ProfilevalidationGateTest
{
	private CessdaMetadataValidatorFactory factory;

	ProfilevalidationGateTest()
	{
		factory = new CessdaMetadataValidatorFactory();
	}

	@Test
	void validateCdcProfile() throws IOException
	{
		// given
		URL documentUrl = getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" );
		URL profileUrl = getClass().getResource( "/cmv-profile-ddi-v32.xml" );

		// when
		ValidationGate.V10 validationGate = new ProfileValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate(
				factory.newDocument( documentUrl ),
				factory.newProfile( profileUrl ) );

		// then
		assertThat( constraintViolations, hasSize( 0 ) );
	}

	@Test
	void validateCmvProfile()
	{
		// given
		URL documentFile = getClass().getResource( "/cmv-profile-ddi-v32.xml" );
		URL profileUrl = getClass().getResource( "/cmv-profile-ddi-v32.xml" );

		// when
		ValidationGate.V10 validationGate = new ProfileValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate(
				factory.newDocument( documentFile ),
				factory.newProfile( profileUrl ) );

		// then
		assertThat( constraintViolations, hasSize( 0 ) );
	}

	@Test
	void validateWithPredicatelessXPathConstraint()
	{
		// https://bitbucket.org/cessda/cessda.cmv.core/issues/39

		// given
		URL documentUrl = getClass().getResource( "/profiles/xpaths-with-predicate.xml" );
		URL profileUrl = getClass().getResource( "/cmv-profile-ddi-v32.xml" );

		// when
		ValidationGate.V10 validationGate = new ProfileValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate(
				factory.newDocument( documentUrl ),
				factory.newProfile( profileUrl ) );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(), containsString( "contains a predicate" ) );
	}

	@Test
	void validateWithCompilableXPathConstraint()
	{
		// given
		URL documentUrl = getClass().getResource( "/profiles/not-compilable-xpaths.xml" );
		URL profileUrl = getClass().getResource( "/cmv-profile-ddi-v32.xml" );

		// when
		ValidationGate.V10 validationGate = new ProfileValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate(
				factory.newDocument( documentUrl ),
				factory.newProfile( profileUrl ) );

		// then
		assertThat( constraintViolations, hasSize( 2 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(), containsString( "A location step was expected" ) );
		assertThat( constraintViolations.get( 1 ).getMessage(), containsString( "Extra illegal tokens" ) );
	}
}
