/*-
 * #%L
 * cmv-core
 * %%
 * Copyright (C) 2020 - 2025 CESSDA ERIC
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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ListRecordsTest
{

	private final TestEnv.V13 testEnv;
	private final CessdaMetadataValidatorFactory factory;

	ListRecordsTest()
	{
		testEnv = DefaultTestEnv.newInstance( ListRecordsTest.class );
		factory = new CessdaMetadataValidatorFactory();
	}

	@Test
	void splitListRecordsResponse() throws IOException, SAXException, NotDocumentException
	{
		File documentURL = testEnv.findTestResourceByName("list-records-response.xml");

		// Load profile
		URL profileUrl = this.getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" );
		Profile profile = factory.newProfile( profileUrl );

		// Split document
		List<Document> documentList = factory.splitListRecordsResponse( new InputSource( documentURL.toURI().toString() ) );
		assertThat(documentList, hasSize(3));

		// Create validation gate
		ValidationGate validationGate = factory.newValidationGate( ValidationGateName.BASIC );

		// Validate each document, should not throw
		for (Document doc : documentList) {
			assertDoesNotThrow( () -> validationGate.validate( doc, profile ) );
		}
	}
}
