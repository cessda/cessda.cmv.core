/*-
 * #%L
 * cmv-core
 * %%
 * Copyright (C) 2020 - 2021 CESSDA ERIC
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.ValidationGateName.BASIC;
import static eu.cessda.cmv.core.ValidationGateName.STANDARD;
import static eu.cessda.cmv.core.ValidationGateName.STRICT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class CdcCodebookDocumentValidationTest
{
	private static final String newTestParameters = "newTestParameters";

	private Profile profile;
	private CessdaMetadataValidatorFactory factory;

	CdcCodebookDocumentValidationTest()
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

	static Stream<TestParameter> newTestParameters()
	{
		TestParameter testParameter;
		List<TestParameter> testParameters = new ArrayList<>();

		testParameter = new TestParameter();
		testParameter.documentName = "/demo-documents/ddi-v25/ukds-7481.xml";
		testParameter.expectedViolationsAtBasicValidationGate = (1 + 0);
		testParameter.expectedViolationsAtStandardValidationGate = 1 + (13 + 0);
		testParameter.expectedViolationsAtStrictValidationGate = 1 + 13 + 8;
		testParameters.add( testParameter );

		testParameter = new TestParameter();
		testParameter.documentName = "/demo-documents/ddi-v25/ukds-2000.xml";
		testParameter.expectedViolationsAtBasicValidationGate = 416;
		testParameter.expectedViolationsAtStandardValidationGate = 416 + 15;
		testParameter.expectedViolationsAtStrictValidationGate = 416 + 14 + 19;
		testParameters.add( testParameter );

		testParameter = new TestParameter();
		testParameter.documentName = "/demo-documents/ddi-v25/fsd-3271.xml";
		testParameter.expectedViolationsAtBasicValidationGate = (2 + 0);
		testParameter.expectedViolationsAtStandardValidationGate = 2 + (7 + 2);
		testParameter.expectedViolationsAtStrictValidationGate = 2 + 9 + (8 + 6);
		testParameters.add( testParameter );

		testParameter = new TestParameter();
		testParameter.documentName = "/demo-documents/ddi-v25/fsd-3307.xml";
		testParameter.expectedViolationsAtBasicValidationGate = (2 + 0);
		testParameter.expectedViolationsAtStandardValidationGate = 2 + (7 + 4);
		testParameter.expectedViolationsAtStrictValidationGate = 2 + 11 + 15;
		testParameters.add( testParameter );

		testParameter = new TestParameter();
		testParameter.documentName = "/demo-documents/ddi-v25/gesis-5100.xml";
		testParameter.expectedViolationsAtBasicValidationGate = (4 + 0);
		testParameter.expectedViolationsAtStandardValidationGate = 4 + (14 + 3);
		testParameter.expectedViolationsAtStrictValidationGate = 4 + 17 + (9 + 7);
		testParameters.add( testParameter );

		testParameter = new TestParameter();
		testParameter.documentName = "/demo-documents/ddi-v25/gesis-5300.xml";
		testParameter.expectedViolationsAtBasicValidationGate = 4 + 0;
		testParameter.expectedViolationsAtStandardValidationGate = 4 + (14 + 3);
		testParameter.expectedViolationsAtStrictValidationGate = 4 + 17 + (11 + 6);
		testParameters.add( testParameter );

		testParameter = new TestParameter();
		testParameter.documentName = "/demo-documents/ddi-v25/gesis-2800.xml";
		testParameter.expectedViolationsAtBasicValidationGate = 4 + 0;
		testParameter.expectedViolationsAtStandardValidationGate = 4 + (14 + 3);
		testParameter.expectedViolationsAtStrictValidationGate = 4 + 17 + (11 + 4);
		testParameters.add( testParameter );

		return testParameters.stream();
	}

	@ParameterizedTest
	@MethodSource( newTestParameters )
	void validateWithBasicValidationGate( TestParameter param )
	{
		// given
		Document document = factory.newDocument( getClass().getResource( param.documentName ) );

		// when
		ValidationGate.V10 validationGate = factory.newValidationGate( BASIC );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );
		// printReport( param.documentName, validationGate.getClass().getSimpleName(),
		// constraintViolations );

		// then
		assertThat( constraintViolations, hasSize( param.expectedViolationsAtBasicValidationGate ) );
	}

	@ParameterizedTest
	@MethodSource( newTestParameters )
	void validateWithStandardValidationGate( TestParameter param )
	{
		// given
		Document document = factory.newDocument( getClass().getResource( param.documentName ) );

		// when
		ValidationGate.V10 validationGate = factory.newValidationGate( STANDARD );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );
		// printReport( param.documentName, validationGate.getClass().getSimpleName(),
		// constraintViolations );

		// then
		assertThat( constraintViolations, hasSize( param.expectedViolationsAtStandardValidationGate ) );
	}

	@ParameterizedTest
	@MethodSource( newTestParameters )
	void validateWithStrictValidationGate( TestParameter param )
	{
		// given
		Document document = factory.newDocument( getClass().getResource( param.documentName ) );

		// when
		ValidationGate.V10 validationGate = factory.newValidationGate( STRICT );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );
		// printReport( param.documentName, validationGate.getClass().getSimpleName(),
		// constraintViolations );

		// then
		assertThat( constraintViolations, hasSize( param.expectedViolationsAtStrictValidationGate ) );
	}

	void printReport( String documentName, String gateName, List<ConstraintViolation> constraintViolations )
	{
		System.out.println( documentName );
		System.out.println( gateName );
		constraintViolations.stream().map( cv -> " -" + cv.getMessage() ).sorted().forEach( System.out::println );
		System.out.println();
	}
}
