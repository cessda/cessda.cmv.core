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

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

class MandatoryNodeConstraintTest
{
	private final TestEnv.V13 testEnv;
	private final CessdaMetadataValidatorFactory factory;

	MandatoryNodeConstraintTest()
	{
		testEnv = DefaultTestEnv.newInstance( MandatoryNodeConstraintTest.class );
		factory = new CessdaMetadataValidatorFactory();
	}

	@Test
	void validate_missing() throws IOException, NotDocumentException
	{
		// given
		File file = testEnv.findTestResourceByName( "ddi-v25/10-document-invalid-missing.xml-invalid" );
		Document document = factory.newDocument( file );
		Profile profile = factory.newProfile( testEnv.findTestResourceByName( "ddi-v25/10-profile.xml" ) );

		// when
		ValidationGate validationGate = new StrictValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(), containsString( "mandatory" ) );
	}

	@Test
	void validate_blank() throws IOException, NotDocumentException
	{
		// given
		File file = testEnv.findTestResourceByName( "ddi-v25/10-document-invalid-blank.xml" );
		Document document = factory.newDocument( file );
		Profile profile = factory.newProfile( testEnv.findTestResourceByName( "ddi-v25/10-profile.xml" ) );

		// when
		ValidationGate validationGate = new StrictValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(), containsString( "blank" ) );
	}

	@Test
	void validate_valid() throws IOException, NotDocumentException
	{
		// given
		File file = testEnv.findTestResourceByName( "ddi-v25/10-document-valid.xml" );
		Document document = factory.newDocument( file );
		Profile profile = factory.newProfile( testEnv.findTestResourceByName( "ddi-v25/10-profile.xml" ) );

		// when
		ValidationGate validationGate = new StrictValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 0 ) );
	}
}
