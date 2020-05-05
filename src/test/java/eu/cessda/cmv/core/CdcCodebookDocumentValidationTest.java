package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.Factory.newDocument;
import static eu.cessda.cmv.core.Factory.newProfile;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class CdcCodebookDocumentValidationTest
{
	private static final String newTestParameters = "newTestParameters";

	private Profile profile = newProfile( getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" ) );

	private static class TestParameter
	{
		String documentName;
		int expectedMandatoryCount;
		int expectedRecommendedCount;
		int expectedOptionalCount;

		public String toString()
		{
			return documentName;
		}
	}

	public static Stream<TestParameter> newTestParameters()
	{
		TestParameter testParameter;
		List<TestParameter> testParameters = new ArrayList<>();

		testParameter = new TestParameter();
		testParameter.documentName = "/demo-documents/ddi-v25/ukds-7481.xml";
		testParameter.expectedMandatoryCount = 10;
		testParameter.expectedRecommendedCount = 9;
		testParameter.expectedOptionalCount = 21;
		testParameters.add( testParameter );

		testParameter = new TestParameter();
		testParameter.documentName = "/demo-documents/ddi-v25/ukds-2000.xml";
		testParameter.expectedMandatoryCount = 9;
		testParameter.expectedRecommendedCount = 9;
		testParameter.expectedOptionalCount = 22;
		testParameters.add( testParameter );

		testParameter = new TestParameter();
		testParameter.documentName = "/demo-documents/ddi-v25/fsd-3271.xml";
		testParameter.expectedMandatoryCount = 8;
		testParameter.expectedRecommendedCount = 5;
		testParameter.expectedOptionalCount = 17;
		testParameters.add( testParameter );

		return testParameters.stream();
	}

	@ParameterizedTest
	@MethodSource( newTestParameters )
	public void validateWithBasicValidationGate( TestParameter param )
	{
		// given
		Document document = newDocument( getClass().getResource( param.documentName ) );

		// when
		ValidationGate.V10 validationGate = new BasicValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( param.expectedMandatoryCount ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv.getMessage().contains( "mandatory" ) )
				.collect( toList() ), hasSize( param.expectedMandatoryCount ) );
	}

	@ParameterizedTest
	@MethodSource( newTestParameters )
	public void validateWithStandardValidationGate( TestParameter param )
	{
		// given
		Document document = newDocument( getClass().getResource( param.documentName ) );

		// when
		ValidationGate.V10 validationGate = new StandardValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( param.expectedMandatoryCount + param.expectedRecommendedCount ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv.getMessage().contains( "mandatory" ) )
				.collect( toList() ), hasSize( param.expectedMandatoryCount ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv.getMessage().contains( "recommended" ) )
				.collect( toList() ), hasSize( param.expectedRecommendedCount ) );
	}

	@ParameterizedTest
	@MethodSource( newTestParameters )
	public void validateWithStrictValidationGate( TestParameter param )
	{
		// given
		Document document = newDocument( getClass().getResource( param.documentName ) );

		// when
		ValidationGate.V10 validationGate = new StrictValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( param.expectedMandatoryCount + param.expectedRecommendedCount
				+ param.expectedOptionalCount ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv.getMessage().contains( "mandatory" ) )
				.collect( toList() ), hasSize( param.expectedMandatoryCount ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv.getMessage().contains( "recommended" ) )
				.collect( toList() ), hasSize( param.expectedRecommendedCount ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv.getMessage().contains( "optional" ) )
				.collect( toList() ), hasSize( param.expectedOptionalCount ) );
	}
}
