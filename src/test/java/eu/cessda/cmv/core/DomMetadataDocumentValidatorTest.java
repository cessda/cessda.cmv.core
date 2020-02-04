package eu.cessda.cmv.core;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

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

		// then
		List<ConstraintViolation.V10> constraintViolations = validator.validate();
		assertThat( constraintViolations, hasSize( 56 ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv instanceof MandatoryNodeConstraintViolation )
				.collect( Collectors.toList() ), hasSize( 31 ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv instanceof RecommendedNodeConstraintViolation )
				.collect( Collectors.toList() ), hasSize( 25 ) );
		assertFalse( constraintViolations.isEmpty() );
	}
}
