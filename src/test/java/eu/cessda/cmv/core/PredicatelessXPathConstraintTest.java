package eu.cessda.cmv.core;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static org.gesis.commons.test.hamcrest.OptionalMatchers.isEmpty;
import static org.gesis.commons.test.hamcrest.OptionalMatchers.isPresent;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.hamcrest.Matcher;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class PredicatelessXPathConstraintTest
{
	private static final String newTestParameters = "newTestParameters";

	private static class TestParameter
	{
		String locationPath;
		Matcher<Optional<?>> expectedConstraintViolation;

		public String toString()
		{
			return locationPath;
		}
	}

	public static Stream<TestParameter> newTestParameters()
	{
		TestParameter testParameter;
		List<TestParameter> testParameters = new ArrayList<>();

		// valid
		testParameter = new TestParameter();
		testParameter.locationPath = "/codeBook/docDscr/citation/holdings";
		testParameter.expectedConstraintViolation = isEmpty();
		testParameters.add( testParameter );

		// invalid
		testParameter = new TestParameter();
		testParameter.locationPath = "/codeBook/stdyDscr/citation/titlStmt/IDNo[@agency='UKDA']";
		testParameter.expectedConstraintViolation = isPresent();
		testParameters.add( testParameter );

		return testParameters.stream();
	}

	@ParameterizedTest
	@MethodSource( newTestParameters )
	public void newValidators( TestParameter testParameter )
	{
		// given
		Constraint.V20 constraint = new PredicatelessXPathConstraint( testParameter.locationPath );

		// when
		List<Validator.V10> validators = constraint.newValidators( mockDocument( testParameter.locationPath ) );

		// then
		assertThat( validators, hasSize( 1 ) );
		assertThat( validators.get( 0 ).validate(), testParameter.expectedConstraintViolation );
	}

	private Document.V10 mockDocument( String locationPath )
	{
		Document.V10 document = mock( Document.V10.class );
		Node node = new Node( "/DDIProfile/Used/@xpath", locationPath, empty() );
		when( document.getNodes( locationPath ) ).thenReturn( asList( node ) );
		return document;
	}
}
