package eu.cessda.cmv.core;

import org.gesis.commons.xml.XmlNotWellformedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DomMetadataDocumentValidatorTest
{
	@Test
	public void validate() throws IOException
	{
		// given
		URL documentFile = getClass().getResource( "/ddi-v25/ukds-7481.xml" );
		URL profileFile = getClass().getResource( "/ddi-v25/cdc25_profile.xml" );

		try ( InputStream documentInputStream = newResource( documentFile ).readInputStream();
			  InputStream profileInputStream = newResource( profileFile ).readInputStream() )
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
		URL documentFile = getClass().getResource( "/ddi-v25/ukds-7481-not-wellformed.xml-invalid" );
		URL profileFile = getClass().getResource( "/ddi-v25/cdc25_profile.xml" );

		try ( InputStream documentInputStream = newResource( documentFile ).readInputStream();
			  InputStream profileInputStream = newResource( profileFile ).readInputStream() )
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
