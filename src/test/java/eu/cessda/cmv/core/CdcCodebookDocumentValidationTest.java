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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static eu.cessda.cmv.core.ValidationGateName.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

class CdcCodebookDocumentValidationTest
{
	private static final String newTestParameters = "newTestParameters";

	private final Profile profile;
	private final CessdaMetadataValidatorFactory factory;

	CdcCodebookDocumentValidationTest() throws IOException, NotDocumentException
	{
		factory = new CessdaMetadataValidatorFactory();
		profile = factory.newProfile( getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" ) );
	}

	private static class TestParameter
	{
		public TestParameter(
			String documentName,
			int expectedViolationsAtBasicValidationGate,
			int expectedViolationsAtStandardValidationGate,
			int expectedViolationsAtStrictValidationGate )
		{
			this.documentName = documentName;
			this.expectedViolationsAtBasicValidationGate = expectedViolationsAtBasicValidationGate;
			this.expectedViolationsAtStandardValidationGate = expectedViolationsAtStandardValidationGate;
			this.expectedViolationsAtStrictValidationGate = expectedViolationsAtStrictValidationGate;
		}

		final String documentName;
		final int expectedViolationsAtBasicValidationGate;
		final int expectedViolationsAtStandardValidationGate;
		final int expectedViolationsAtStrictValidationGate;

		public String toString()
		{
			return documentName;
		}
	}

	static Stream<TestParameter> newTestParameters()
	{
		List<TestParameter> testParameters = new ArrayList<>();

		testParameters.add( new TestParameter(
			"/demo-documents/ddi-v25/ukds-7481.xml",
			1,
			1 + ( 13 ),
			1 + 13 + 8
		));

		testParameters.add( new TestParameter(
			"/demo-documents/ddi-v25/ukds-2000.xml",
			216,
			216 + 16,
			216 + 16 + 17
		));

		testParameters.add( new TestParameter(
			"/demo-documents/ddi-v25/fsd-3271.xml",
			2 ,
			2 + ( 7 + 2 ),
			2 + 9 + 7
		));

		testParameters.add( new TestParameter(
			"/demo-documents/ddi-v25/fsd-3307.xml",
			2,
			2 + ( 7 + 4 ),
			2 + 11 + 8
		));

		testParameters.add( new TestParameter(
			"/demo-documents/ddi-v25/fsd-3307-oaipmh.xml",
			2,
			2 + ( 7 + 4 ),
			2 + 11 + 8
		));

		testParameters.add( new TestParameter(
			"/demo-documents/ddi-v25/gesis-5100.xml",
			4,
			4 + ( 15 + 3 ),
			4 + 17 + (9 + 3)
		));

		testParameters.add( new TestParameter(
			"/demo-documents/ddi-v25/gesis-5300.xml",
			4,
			4 + ( 15 + 3 ),
			4 + 18 + ( 10 )
		));

		testParameters.add( new TestParameter(
			"/demo-documents/ddi-v25/gesis-2800.xml",
			4,
			4 + ( 15 + 3 ),
			4 + 18 + ( 10 )
		));

		testParameters.add( new TestParameter(
			"/ddi-with-namespace-prefix.xml",
			10,
			10 + ( 26 ),
			10 + 26 + ( 23 )
		) );

		return testParameters.stream();
	}

	@ParameterizedTest
	@MethodSource( newTestParameters )
	void validateWithBasicValidationGate( TestParameter param ) throws IOException, NotDocumentException
	{
		// given
		Document document = factory.newDocument( getClass().getResource( param.documentName ) );

		// when
		ValidationGate validationGate = factory.newValidationGate( BASIC );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );
		// printReport( param.documentName, validationGate.getClass().getSimpleName(),
		// constraintViolations );

		// then
		assertThat( constraintViolations, hasSize( param.expectedViolationsAtBasicValidationGate ) );
	}

	@ParameterizedTest
	@MethodSource( newTestParameters )
	void validateWithStandardValidationGate( TestParameter param ) throws IOException, NotDocumentException
	{
		// given
		Document document = factory.newDocument( getClass().getResource( param.documentName ) );

		// when
		ValidationGate validationGate = factory.newValidationGate( STANDARD );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );
		// printReport( param.documentName, validationGate.getClass().getSimpleName(),
		// constraintViolations );

		// then
		assertThat( constraintViolations, hasSize( param.expectedViolationsAtStandardValidationGate ) );
	}

	@ParameterizedTest
	@MethodSource( newTestParameters )
	void validateWithStrictValidationGate( TestParameter param ) throws IOException, NotDocumentException
	{
		// given
		Document document = factory.newDocument( getClass().getResource( param.documentName ) );

		// when
		ValidationGate validationGate = factory.newValidationGate( STRICT );
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
