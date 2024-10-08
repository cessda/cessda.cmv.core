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

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static eu.cessda.cmv.core.ValidationGateName.EXTENDED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

class FixedValueNodeConstraintTest
{
	private final TestEnv.V13 testEnv;
	private final Profile profile;
	private final CessdaMetadataValidatorFactory factory;

	FixedValueNodeConstraintTest() throws IOException, NotDocumentException
	{
		testEnv = DefaultTestEnv.newInstance( FixedValueNodeConstraintTest.class );
		factory = new CessdaMetadataValidatorFactory();
		profile = factory.newProfile( testEnv.findTestResourceByName( "13-profile.xml" ) );
	}

	@Test
	void validate_valid() throws IOException, NotDocumentException
	{
		// given
		Document document = factory.newDocument( testEnv.findTestResourceByName( "13-document-valid-1.xml" ) );

		// when
		ValidationGate validationGate = factory.newValidationGate( EXTENDED );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( "Unexpected constraint violations: " + constraintViolations.toString(), constraintViolations, hasSize( 0 ) );
	}

	@Test
	void validate_invalid_inequalFixedValue() throws IOException, NotDocumentException
	{
		// given
		Document document = factory.newDocument( testEnv.findTestResourceByName( "13-document-invalid-1.xml" ) );

		// when
		ValidationGate validationGate = factory.newValidationGate( EXTENDED );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).toString(),
				equalTo( "'DDI Analyseeinheit' is not equal to fixed value 'DDI Analysis Unit' in '/ddi:codeBook/ddi:stdyDscr/ddi:stdyInfo/ddi:sumDscr/ddi:anlyUnit/ddi:concept/@vocab' (lineNumber: 13, columnNumber: 44)" ) );
	}

	@Test
	void validate_invalid_missing() throws IOException, NotDocumentException
	{
		// given
		Document document = factory.newDocument( testEnv.findTestResourceByName( "13-document-invalid-2.xml" ) );

		// when
		ValidationGate validationGate = factory.newValidationGate( EXTENDED );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(),
				equalTo( "'/ddi:codeBook/ddi:stdyDscr/ddi:stdyInfo/ddi:sumDscr/ddi:anlyUnit/ddi:concept/@vocab' is optional" ) );
	}
}
