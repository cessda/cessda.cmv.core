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
		List<ConstraintViolation> constraintViolations = validatationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 2 ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv.getMessage().contains( "mandatory" ) )
				.collect( Collectors.toList() ), hasSize( 1 ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv.getMessage().contains( "recommended" ) )
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
			Document document = new DomCodebookDocument( documentInputStream );
			Profile profile = new DomProfile( profileInputStream );

			// when
			ValidationGate.V10 validationGate = new StandardValidationGate();
			List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

			// then
			assertThat( constraintViolations, hasSize( 40 ) );
			assertThat( constraintViolations.stream()
					.filter( cv -> cv.getMessage().contains( "mandatory" ) )
					.collect( Collectors.toList() ), hasSize( 10 ) );
			assertThat( constraintViolations.stream()
					.filter( cv -> cv.getMessage().contains( "recommended" ) )
					.collect( Collectors.toList() ), hasSize( 30 ) );

			JaxbValidationReport validationReport = new JaxbValidationReport();
			validationReport.setConstraintViolations( constraintViolations );
			System.out.println( validationReport.toString() );
		}
	}
}
