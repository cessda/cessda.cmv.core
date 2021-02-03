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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

import java.io.File;
import java.util.List;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.gesis.commons.xml.SaxXercesAgainstSchemaValidator;
import org.gesis.commons.xml.ddi.Ddi251ClasspathEntityResolver;
import org.junit.jupiter.api.Test;

class MandatoryNodeIfParentPresentConstraintTest
{
	private TestEnv.V14 testEnv;
	private CessdaMetadataValidatorFactory factory;

	MandatoryNodeIfParentPresentConstraintTest()
	{
		testEnv = DefaultTestEnv.newInstance( MandatoryNodeIfParentPresentConstraintTest.class );
		factory = new CessdaMetadataValidatorFactory();
		SaxXercesAgainstSchemaValidator xmlValidator = new SaxXercesAgainstSchemaValidator();
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/22-document-invalid-1.xml" ),
				new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/22-document-invalid-2.xml" ),
				new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/22-document-valid-1.xml" ),
				new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/22-document-valid-2.xml" ),
				new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/22-profile.xml" ) );
	}

	@Test
	void validate_missing()
	{
		// given
		File file = testEnv.findTestResourceByName( "22-document-invalid-1.xml" );
		Document document = factory.newDocument( file );
		Profile profile = factory.newProfile( testEnv.findTestResourceByName( "22-profile.xml" ) );

		// when
		ValidationGate.V10 validationGate = factory.newValidationGate( BASIC );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(), containsString( "mandatory in" ) );
	}

	@Test
	void validate_blank()
	{
		// given
		File file = testEnv.findTestResourceByName( "22-document-invalid-2.xml" );
		Document document = factory.newDocument( file );
		Profile profile = factory.newProfile( testEnv.findTestResourceByName( "22-profile.xml" ) );

		// when
		ValidationGate.V10 validationGate = factory.newValidationGate( BASIC );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(), containsString( "blank" ) );
	}

	@Test
	void validate_valid()
	{
		// given
		File file = testEnv.findTestResourceByName( "22-document-valid-1.xml" );
		Document document = factory.newDocument( file );
		Profile profile = factory.newProfile( testEnv.findTestResourceByName( "22-profile.xml" ) );

		// when
		ValidationGate.V10 validationGate = factory.newValidationGate( BASIC );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 0 ) );
	}

	@Test
	void validate_valid_parentNotPresent()
	{
		// given
		File file = testEnv.findTestResourceByName( "22-document-valid-2.xml" );
		Document document = factory.newDocument( file );
		Profile profile = factory.newProfile( testEnv.findTestResourceByName( "22-profile.xml" ) );

		// when
		ValidationGate.V10 validationGate = factory.newValidationGate( BASIC );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 0 ) );
	}
}
