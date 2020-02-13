package eu.cessda.cmv.core;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.gesis.commons.xml.XmlNotWellformedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class DomMetadataDocumentValidatorTest
{
	@Test
	public void validate()
	{
		// given
		URI documentUri = new File( "src/test/resources/ddi-v25/ukds-7481.xml" ).toURI();
		URI profileUri = new File( "src/test/resources/ddi-v25/cdc_profile.xml" ).toURI();

		// when
		Constraint.V10 validator = new DomMetadataDocumentValidator( documentUri, profileUri );
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

	@Test
	public void constructWithNotWellformedMetadataDocument()
	{
		// given
		URI documentUri = new File( "src/test/resources/ddi-v25/ukds-7481-not-wellformed.xml" ).toURI();
		URI profileUri = new File( "src/test/resources/ddi-v25/cdc_profile.xml" ).toURI();

		// when
		Executable executable = () -> new DomMetadataDocumentValidator( documentUri, profileUri );

		// then
		assertThrows( XmlNotWellformedException.class, executable );
	}
}
