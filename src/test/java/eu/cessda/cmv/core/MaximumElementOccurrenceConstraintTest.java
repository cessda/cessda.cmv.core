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

import static eu.cessda.cmv.core.ValidationGateName.STRICT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

class MaximumElementOccurrenceConstraintTest
{
	private final TestEnv.V13 testEnv;
	private final CessdaMetadataValidatorFactory factory;

	MaximumElementOccurrenceConstraintTest()
	{
		testEnv = DefaultTestEnv.newInstance( MaximumElementOccurrenceConstraintTest.class );
		factory = new CessdaMetadataValidatorFactory();

	}


	@Test
	void validate_invalid() throws IOException, NotDocumentException
	{
		// given
		Document document = factory.newDocument( testEnv.findTestResourceByName( "bad-case.xml" ) );
		Profile profile = factory.newProfile( testEnv.findTestResourceByName( "profile.xml" ) );

		// when
		ValidationGate validationGate = factory.newValidationGate( STRICT );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
		assertThat( constraintViolations.get( 0 ).getMessage(), containsString( "occurs more than maximal count of" ) );
	}

	@Test
	void validate_valid() throws IOException, NotDocumentException
	{
		// given
		Document document = factory.newDocument( testEnv.findTestResourceByName( "good-case.xml" ) );
		Profile profile = factory.newProfile( testEnv.findTestResourceByName( "profile.xml" ).toURI().toURL() );

		// when
		ValidationGate validationGate = factory.newValidationGate( STRICT );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 0 ) );
	}
}
