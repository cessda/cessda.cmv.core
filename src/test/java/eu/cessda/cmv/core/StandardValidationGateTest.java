package eu.cessda.cmv.core;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class StandardValidationGateTest
{
	@Test
	void validate()
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
}
