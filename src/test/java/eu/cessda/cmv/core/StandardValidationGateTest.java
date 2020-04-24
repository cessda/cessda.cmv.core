package eu.cessda.cmv.core;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.gesis.commons.xml.ddi.DdiInputStream;
import org.junit.jupiter.api.Test;

public class StandardValidationGateTest
{
	@Test
	public void validate()
	{
		// given
		Document.V10 document = mock( Document.V10.class );
		when( document.getNodes( anyString() ) ).thenReturn( emptyList() );
		Profile.V10 profile = mock( Profile.V10.class );
		when( profile.getConstraints() ).thenReturn( asList(
				new MandatoryNodeConstraint( "/path/to/mandatory/node" ),
				new RecommendedNodeConstraint( "/path/to/recommended/node" ) ) );

		// when
		ValidationGate.V10 validatationGate = new StandardValidationGate();
		ValidationReport.V10 validationReport = validatationGate.validate( document, profile );

		// then
		assertThat( validationReport.getConstraintViolations(), hasSize( 2 ) );
		assertThat( validationReport.getConstraintViolations().stream()
				.filter( cv -> cv instanceof MandatoryNodeConstraintViolation )
				.collect( Collectors.toList() ), hasSize( 1 ) );
		assertThat( validationReport.getConstraintViolations().stream()
				.filter( cv -> cv instanceof RecommendedNodeConstraintViolation )
				.collect( Collectors.toList() ), hasSize( 1 ) );
	}

	@Test
	public void validate_cdc_ukds() throws IOException
	{
		// given
		URL documentFile = getClass().getResource( "/ddi-v25/ukds-7481.xml" );
		URL profileFile = getClass().getResource( "/ddi-v25/cdc25_profile.xml" );

		try ( DdiInputStream documentInputStream = new DdiInputStream( newResource( documentFile ).readInputStream() );
				DdiInputStream profileInputStream = new DdiInputStream( newResource( profileFile ).readInputStream() ) )
		{
			Document document = new DomDocument( documentInputStream );
			Profile profile = new DomProfile( profileInputStream );

			// when
			ValidationGate.V10 validationGate = new StandardValidationGate();
			ValidationReport.V10 report = validationGate.validate( document, profile );

			// then
			List<ConstraintViolation.V10> constraintViolations = report.getConstraintViolations();
			assertThat( constraintViolations, hasSize( 40 ) );
			assertThat( constraintViolations.stream()
					.filter( cv -> cv instanceof MandatoryNodeConstraintViolation )
					.collect( Collectors.toList() ), hasSize( 10 ) );
			assertThat( constraintViolations.stream()
					.filter( cv -> cv instanceof RecommendedNodeConstraintViolation )
					.collect( Collectors.toList() ), hasSize( 30 ) );

			System.out.println();
			System.out.println( profileFile );
			System.out.println( documentFile );
			constraintViolations.stream()
					.map( ConstraintViolation.V10::getMessage )
					.forEach( System.out::println );
			System.out.println();
		}
	}
}
