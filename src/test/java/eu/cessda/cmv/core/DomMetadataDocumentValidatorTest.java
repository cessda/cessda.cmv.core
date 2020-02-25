package eu.cessda.cmv.core;

import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.xml.XmlNotWellformedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class DomMetadataDocumentValidatorTest
{
	@Test
	public void validate()
	{
		// given
		File file = new File( "src/test/resources/ddi-v25/ukds-7481.xml" );
		Resource documentResource = newResource( file );
		file = new File( "src/test/resources/ddi-v25/cdc_profile.xml" );
		Resource profileResource = newResource( file );

		// when
		Constraint.V10 validator = new DomMetadataDocumentValidator(
				documentResource.readInputStream(),
				profileResource.readInputStream() );
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
		File file = new File( "src/test/resources/ddi-v25/ukds-7481-not-wellformed.xml" );
		Resource documentResource = newResource( file );
		file = new File( "src/test/resources/ddi-v25/cdc_profile.xml" );
		Resource profileResource = newResource( file );

		// when
		Executable executable = () -> new DomMetadataDocumentValidator(
				documentResource.readInputStream(),
				profileResource.readInputStream() );

		// then
		assertThrows( XmlNotWellformedException.class, executable );
	}
}
