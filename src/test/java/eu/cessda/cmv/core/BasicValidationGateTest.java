package eu.cessda.cmv.core;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
		ValidationReport.V10 validationReport = validatationGate.validate( document, profile );

		// then
		assertThat( validationReport.getConstraintViolations(), hasSize( 1 ) );
		assertThat( validationReport.getConstraintViolations().stream()
				.filter( cv -> cv instanceof MandatoryNodeConstraintViolation )
				.collect( Collectors.toList() ), hasSize( 1 ) );
	}

	@Test
	public void valid()
	{
		// given
		Document.V10 document = mock( Document.V10.class );
		when( document.getNodes( "/path/to/mandatory/node" ) )
				.thenReturn( asList( new Node( "at-least-one" ) ) );
		Profile.V10 profile = mock( Profile.V10.class );
		when( profile.getConstraints() ).thenReturn( asList(
				new MandatoryNodeConstraint( "/path/to/mandatory/node" ),
				new RecommendedNodeConstraint( "/path/to/recommended/node" ) ) );

		// when
		ValidationGate.V10 validatationGate = new BasicValidationGate();
		ValidationReport.V10 validationReport = validatationGate.validate( document, profile );

		// then
		assertThat( validationReport.getConstraintViolations(), hasSize( 0 ) );
	}
}
