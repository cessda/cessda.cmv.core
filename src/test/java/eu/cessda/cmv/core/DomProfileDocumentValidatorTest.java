package eu.cessda.cmv.core;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.Test;

public class DomProfileDocumentValidatorTest
{
	@Test
	public void validateCdcProfile()
	{
		// given: the profile as metadata document
		URI documentUri = new File( "src/test/resources/ddi-v25/cdc_profile.xml" ).toURI();

		// when
		Constraint.V10 validator = new DomProfileDocumentValidator( documentUri );
		List<ConstraintViolation.V10> constraintViolations = validator.validate();

		// then
		assertThat( constraintViolations, hasSize( 11 ) );
	}

	@Test
	public void validateCmvProfile()
	{
		// given: the profile as metadata document
		URI documentUri = new File( "src/main/resources/cmv-profile-ddi-v32.xml" ).toURI();

		// when
		Constraint.V10 validator = new DomProfileDocumentValidator( documentUri );
		List<ConstraintViolation.V10> constraintViolations = validator.validate();

		// then
		assertThat( constraintViolations, hasSize( 0 ) );
	}

	@Test
	public void validate()
	{
		URI documentUri = new File( "src/test/resources/profiles/xpaths-with-predicate.xml" ).toURI();

		// when
		Constraint.V10 validator = new DomProfileDocumentValidator( documentUri );
		List<ConstraintViolation.V10> constraintViolations = validator.validate();

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
	}

}
