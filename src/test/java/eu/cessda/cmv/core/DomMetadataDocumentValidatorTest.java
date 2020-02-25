package eu.cessda.cmv.core;

import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.gesis.commons.xml.XmlNotWellformedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class DomMetadataDocumentValidatorTest
{
	@Test
	public void validate() throws IOException
	{
		// given
		File documentFile = new File( "src/test/resources/ddi-v25/ukds-7481.xml" );
		File profileFile = new File( "src/test/resources/ddi-v25/cdc_profile.xml" );

		try (InputStream documentInputStream = newResource( documentFile ).readInputStream();
				InputStream profileInputStream = newResource( profileFile ).readInputStream())
		{
			// when
			Constraint.V10 validator = new DomMetadataDocumentValidator(
					documentInputStream,
					profileInputStream );
			List<ConstraintViolation.V10> constraintViolations = validator.validate();

			// then
			assertThat( constraintViolations, hasSize( 51 ) );
			assertThat( constraintViolations.stream()
					.filter( cv -> cv instanceof MandatoryNodeConstraintViolation )
					.collect( Collectors.toList() ), hasSize( 28 ) );
			assertThat( constraintViolations.stream()
					.filter( cv -> cv instanceof RecommendedNodeConstraintViolation )
					.collect( Collectors.toList() ), hasSize( 23 ) );
		}
	}

	@Test
	public void constructWithNotWellformedMetadataDocument() throws IOException
	{
		// given
		File documentFile = new File( "src/test/resources/ddi-v25/ukds-7481-not-wellformed.xml" );
		File profileFile = new File( "src/test/resources/ddi-v25/cdc_profile.xml" );

		try (InputStream documentInputStream = newResource( documentFile ).readInputStream();
				InputStream profileInputStream = newResource( profileFile ).readInputStream())
		{
			// when
			Executable executable = () -> new DomMetadataDocumentValidator(
					documentInputStream,
					profileInputStream );
			// then
			assertThrows( XmlNotWellformedException.class, executable );
		}
	}
}
