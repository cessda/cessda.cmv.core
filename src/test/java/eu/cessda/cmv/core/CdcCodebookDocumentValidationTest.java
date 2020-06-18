package eu.cessda.cmv.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class CdcCodebookDocumentValidationTest
{
	private static final String newTestParameters = "newTestParameters";

	private Profile profile;
	private CessdaMetadataValidatorFactory factory;

	public CdcCodebookDocumentValidationTest()
	{
		factory = new CessdaMetadataValidatorFactory();
		profile = factory.newProfile( getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" ) );
	}

	private static class TestParameter
	{
		String documentName;
		int expectedViolationsAtBasicValidationGate;
		int expectedViolationsAtStandardValidationGate;
		int expectedViolationsAtStrictValidationGate;

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
		testParameter.expectedViolationsAtBasicValidationGate = 10;
		testParameter.expectedViolationsAtStandardValidationGate = 10 + 12;
		testParameter.expectedViolationsAtStrictValidationGate = 10 + 12 + 18;
		testParameters.add( testParameter );

		testParameter = new TestParameter();
		testParameter.documentName = "/demo-documents/ddi-v25/ukds-2000.xml";
		testParameter.expectedViolationsAtBasicValidationGate = 9;
		testParameter.expectedViolationsAtStandardValidationGate = 9 + 12;
		testParameter.expectedViolationsAtStrictValidationGate = 9 + 12 + 19;
		testParameters.add( testParameter );

		testParameter = new TestParameter();
		testParameter.documentName = "/demo-documents/ddi-v25/fsd-3271.xml";
		// (mandatory + blank)
		testParameter.expectedViolationsAtBasicValidationGate = (8 + 1);
		// (mandatory + blank) + (recommended + blank)
		testParameter.expectedViolationsAtStandardValidationGate = (8 + 1) + (6 + 2);
		// (mandatory + blank) + (recommended + blank) + (optional + blank)
		testParameter.expectedViolationsAtStrictValidationGate = (8 + 1) + (6 + 2) + (16 + 6);
		testParameters.add( testParameter );

		testParameter = new TestParameter();
		testParameter.documentName = "/demo-documents/ddi-v25/gesis-5100.xml";
		testParameter.expectedViolationsAtBasicValidationGate = 9;
		testParameter.expectedViolationsAtStandardValidationGate = 9 + 10 + 3;
		testParameter.expectedViolationsAtStrictValidationGate = 9 + 10 + 18 + 7;
		testParameters.add( testParameter );

		testParameter = new TestParameter();
		testParameter.documentName = "/demo-documents/ddi-v25/gesis-5300.xml";
		testParameter.expectedViolationsAtBasicValidationGate = (8 + 1);
		testParameter.expectedViolationsAtStandardValidationGate = (8 + 1) + (10 + 3);
		testParameter.expectedViolationsAtStrictValidationGate = (8 + 1) + (10 + 3) + (18 + 5);
		testParameters.add( testParameter );

		testParameter = new TestParameter();
		testParameter.documentName = "/demo-documents/ddi-v25/gesis-2800.xml";
		testParameter.expectedViolationsAtBasicValidationGate = 9;
		testParameter.expectedViolationsAtStandardValidationGate = 9 + (10 + 3);
		testParameter.expectedViolationsAtStrictValidationGate = 9 + (10 + 3) + (17 + 4);
		testParameters.add( testParameter );

		return testParameters.stream();
	}

	@ParameterizedTest
	@MethodSource( newTestParameters )
	public void validateWithBasicValidationGate( TestParameter param )
	{
		// given
		Document document = factory.newDocument( getClass().getResource( param.documentName ) );

		// when
		ValidationGate.V10 validationGate = new BasicValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( param.expectedViolationsAtBasicValidationGate ) );
	}

	@ParameterizedTest
	@MethodSource( newTestParameters )
	public void validateWithStandardValidationGate( TestParameter param )
	{
		// given
		Document document = factory.newDocument( getClass().getResource( param.documentName ) );

		// when
		ValidationGate.V10 validationGate = new StandardValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( param.expectedViolationsAtStandardValidationGate ) );
	}

	@ParameterizedTest
	@MethodSource( newTestParameters )
	public void validateWithStrictValidationGate( TestParameter param )
	{
		// given
		Document document = factory.newDocument( getClass().getResource( param.documentName ) );

		// when
		ValidationGate.V10 validationGate = new StrictValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( param.expectedViolationsAtStrictValidationGate ) );
	}
}
