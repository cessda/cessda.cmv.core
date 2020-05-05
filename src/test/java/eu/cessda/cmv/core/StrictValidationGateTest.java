package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.Factory.newDocument;
import static eu.cessda.cmv.core.Factory.newProfile;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class StrictValidationGateTest
{
	@Test
	public void validate_cdc_ukds7481() throws IOException
	{
		// given
		Document document = newDocument( getClass().getResource( "/ddi-v25/ukds-7481.xml" ) );
		Profile profile = newProfile( getClass().getResource( "/ddi-v25/cdc25_profile.xml" ) );

		// when
		ValidationGate.V10 validationGate = new StrictValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 40 ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv.getMessage().contains( "mandatory" ) )
				.collect( Collectors.toList() ), hasSize( 10 ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv.getMessage().contains( "recommended" ) )
				.collect( Collectors.toList() ), hasSize( 9 ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv.getMessage().contains( "optional" ) )
				.collect( Collectors.toList() ), hasSize( 21 ) );

	}

	@Test
	public void validate_cdc_ukds2000() throws IOException
	{
		// given
		Document document = newDocument( getClass().getResource( "/ddi-v25/ukds-2000.xml" ) );
		Profile profile = newProfile( getClass().getResource( "/ddi-v25/cdc25_profile.xml" ) );

		// when
		ValidationGate.V10 validationGate = new StrictValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 40 ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv.getMessage().contains( "mandatory" ) )
				.collect( Collectors.toList() ), hasSize( 9 ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv.getMessage().contains( "recommended" ) )
				.collect( Collectors.toList() ), hasSize( 9 ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv.getMessage().contains( "optional" ) )
				.collect( Collectors.toList() ), hasSize( 22 ) );
	}

	@Test
	public void validate_cdc_fsd()
	{
		// given
		// https://bitbucket.org/cessda/cessda.cmv.core/issues/47
		Document document = newDocument( getClass().getResource( "/ddi-v25/fsd-3271.xml" ) );
		Profile profile = newProfile( getClass().getResource( "/ddi-v25/cdc25_profile.xml" ) );

		// when
		ValidationGate.V10 validationGate = new StrictValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 30 ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv.getMessage().contains( "mandatory" ) )
				.collect( Collectors.toList() ), hasSize( 8 ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv.getMessage().contains( "recommended" ) )
				.collect( Collectors.toList() ), hasSize( 5 ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv.getMessage().contains( "optional" ) )
				.collect( Collectors.toList() ), hasSize( 17 ) );
	}
}
