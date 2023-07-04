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

import java.util.List;

import static eu.cessda.cmv.core.ValidationGateName.BASICPLUS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class CodeValueOfControlledVocabularyConstraintTest
{
	private final TestEnv.V13 testEnv;
	private final Profile profile;
	private final CessdaMetadataValidatorFactory factory;

	CodeValueOfControlledVocabularyConstraintTest()
	{
		testEnv = DefaultTestEnv.newInstance( CodeValueOfControlledVocabularyConstraintTest.class );
		factory = new CessdaMetadataValidatorFactory();
		profile = factory.newProfile( testEnv.findTestResourceByName( "9-profile.xml" ) );
		assertThat( profile.getConstraints().stream()
				.filter( ControlledVocabularyRepositoryConstraint.class::isInstance )
				.count(), is( 2L ) );
		assertThat( profile.getConstraints().stream()
				.filter( CodeValueOfControlledVocabularyConstraint.class::isInstance )
				.count(), is( 1L ) );
	}

	@Test
	void validate_invalid_not_element()
	{
		// given
		Document document = factory.newDocument( testEnv.findTestResourceByName( "9-document-invalid-1.xml" ) );

		// when
		ValidationGate validationGate = factory.newValidationGate( BASICPLUS );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(),
				equalTo( "Code value 'Person' in '/codeBook/stdyDscr/stdyInfo/sumDscr/anlyUnit/concept' is not element of the controlled vocabulary in 'https://vocabularies.cessda.eu/v2/vocabularies/AnalysisUnit/2.0?languageVersion=en-2.0' (lineNumber: 16)" ) );
	}

	@Test
	void validate_invalid_missing_url()
	{
		// given
		Document document = factory.newDocument( testEnv.findTestResourceByName( "9-document-invalid-2.xml" ) );

		// when
		ValidationGate validationGate = factory.newValidationGate( BASICPLUS );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(),
				equalTo( "Code value 'Family.HouseholdFamily' in '/codeBook/stdyDscr/stdyInfo/sumDscr/anlyUnit/concept' cannot be validated because no controlled vocabulary is declared (lineNumber: 28)" ) );
	}

	@Test
	void validate_valid()
	{
		// given
		Document document = factory.newDocument( testEnv.findTestResourceByName( "9-document-valid.xml" ) );

		// when
		ValidationGate validationGate = factory.newValidationGate( BASICPLUS );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 0 ) );
	}
}
