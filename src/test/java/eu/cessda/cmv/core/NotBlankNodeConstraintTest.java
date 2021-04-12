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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

import java.util.List;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.gesis.commons.xml.SaxXercesAgainstSchemaValidator;
import org.gesis.commons.xml.ddi.Ddi251ClasspathEntityResolver;
import org.junit.jupiter.api.Test;

class NotBlankNodeConstraintTest
{
	private TestEnv.V14 testEnv;
	private CessdaMetadataValidatorFactory factory;
	private ValidationGate.V10 validationGate;

	NotBlankNodeConstraintTest()
	{
		testEnv = DefaultTestEnv.newInstance( NotBlankNodeConstraintTest.class );
		SaxXercesAgainstSchemaValidator xmlValidator = new SaxXercesAgainstSchemaValidator();
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/57-document-invalid-blank.xml" ),
				new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/57-document-valid.xml" ),
				new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/57-profile.xml" ) );
		factory = new CessdaMetadataValidatorFactory();
		validationGate = new StrictValidationGate();
	}

	@Test
	void validate_invalid_blank()
	{
		// given
		Profile profile = factory.newProfile( testEnv.findTestResourceByName( "ddi-v25/57-profile.xml" ) );
		Document document = factory.newDocument( testEnv.findTestResourceByName( "ddi-v25/57-document-invalid-blank.xml" ) );

		// when
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(), containsString( "is blank" ) );
	}

	@Test
	void validate_valid()
	{
		// given
		Profile profile = factory.newProfile( testEnv.findTestResourceByName( "ddi-v25/57-profile.xml" ) );

		Document document = factory.newDocument( testEnv.findTestResourceByName( "ddi-v25/57-document-valid.xml" ) );

		// when
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 0 ) );
	}

	@Test
	void validate_invalid_missing()
	{
		// given
		Profile profile = factory.newProfile( testEnv.findTestResourceByName( "ddi-v25/57-profile.xml" ) );
		Document document = factory.newDocument( testEnv.findTestResourceByName( "ddi-v25/57-document-invalid-missing.xml-invalid" ) );

		// when
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(), containsString( "is optional" ) );
	}
}
