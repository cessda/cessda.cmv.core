package eu.cessda.cmv.core;

import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.gesis.commons.resource.Resource;
import org.junit.jupiter.api.Test;

public class DomProfileDocumentValidatorTest
{
	@Test
	public void validateCdcProfile() throws IOException
	{
		// given: the profile as metadata document
		File documentFile = new File( "src/test/resources/ddi-v25/cdc25_profile.xml" );
		try (InputStream documentInputStream = newResource( documentFile ).readInputStream())
		{
			// when
			Constraint.V10 validator = new DomProfileDocumentValidator( documentInputStream );
			List<ConstraintViolation.V10> constraintViolations = validator.validate();

			// then
			assertThat( constraintViolations, hasSize( 11 ) );
			constraintViolations.stream()
					.map( ConstraintViolation.V10::getMessage )
					.forEach( System.out::println );
		}
	}

	@Test
	public void validateCmvProfile()
	{
		// given: the profile as metadata document
		File file = new File( "src/main/resources/cmv-profile-ddi-v32.xml" );
		Resource documentResource = newResource( file );

		// when
		Constraint.V10 validator = new DomProfileDocumentValidator( documentResource.readInputStream() );
		List<ConstraintViolation.V10> constraintViolations = validator.validate();

		// then
		assertThat( constraintViolations, hasSize( 0 ) );
	}

	@Test
	public void validateXpathWithPredicate()
	{
		// https://bitbucket.org/cessda/cessda.cmv.core/issues/39
		File file = new File( "src/test/resources/profiles/xpaths-with-predicate.xml" );
		Resource documentResource = newResource( file );

		// when
		Constraint.V10 validator = new DomProfileDocumentValidator( documentResource.readInputStream() );
		List<ConstraintViolation.V10> constraintViolations = validator.validate();

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
	}
}
