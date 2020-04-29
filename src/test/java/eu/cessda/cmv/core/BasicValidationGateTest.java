package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.Factory.newDocument;
import static eu.cessda.cmv.core.Factory.newProfile;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class BasicValidationGateTest
{
	@Test
	public void invalid()
	{
		// given
		Document.V10 document = mock( Document.V10.class );
		when( document.getNodes( anyString() ) ).thenReturn( emptyList() );
		Profile.V10 profile = mock( Profile.V10.class );
		when( profile.getConstraints() ).thenReturn( asList(
				new MandatoryNodeConstraint( "/path/to/mandatory/node" ),
				new RecommendedNodeConstraint( "/path/to/recommended/node" ) ) );

		// when
		ValidationGate.V10 validatationGate = new BasicValidationGate();
		List<ConstraintViolation> constraintViolations = validatationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.stream()
				.collect( Collectors.toList() ), hasSize( 1 ) );
	}

	@Test
	public void valid()
	{
		// given
		Document.V10 document = mock( Document.V10.class );
		when( document.getNodes( "/path/to/mandatory/node" ) )
				.thenReturn( asList( new Node( "/path/to/mandatory/node", "at-least-one", empty() ) ) );
		Profile.V10 profile = mock( Profile.V10.class );
		when( profile.getConstraints() ).thenReturn( asList(
				new MandatoryNodeConstraint( "/path/to/mandatory/node" ),
				new RecommendedNodeConstraint( "/path/to/recommended/node" ) ) );

		// when
		ValidationGate.V10 validatationGate = new BasicValidationGate();
		List<ConstraintViolation> constraintViolations = validatationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 0 ) );
	}

	@Test
	public void validate_cdc_fsd()
	{
		// given
		// https://bitbucket.org/cessda/cessda.cmv.core/issues/47
		Document document = newDocument( getClass().getResource( "/ddi-v25/fsd-3271.xml" ) );
		Profile profile = newProfile( getClass().getResource( "/ddi-v25/cdc25_profile.xml" ) );

		// when
		ValidationGate.V10 validationGate = new BasicValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 8 ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv.getMessage().contains( "mandatory" ) )
				.collect( Collectors.toList() ), hasSize( 8 ) );
	}
}
