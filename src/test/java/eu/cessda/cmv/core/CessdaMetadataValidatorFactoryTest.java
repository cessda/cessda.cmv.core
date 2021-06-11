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

import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.xml.DomDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CessdaMetadataValidatorFactoryTest
{
	private CessdaMetadataValidatorFactory factory;

	CessdaMetadataValidatorFactoryTest()
	{
		factory = new CessdaMetadataValidatorFactory();
	}

	@Test
	void newDomDocument()
	{
		File file = new File( "src/main/resources/cmv-profile-ddi-v32.xml" );
		DomDocument.V11 document = factory.newDomDocument( file );
		assertThat( document.selectNode( "/pr:DDIProfile" ), notNullValue() );
	}

	@ParameterizedTest
	@ValueSource(
			strings = {
					"https://bitbucket.org/cessda/cessda.cmv.core/raw/815a9aa0688300aea56b7ff31bdb99ec9714729d/src/main/resources/demo-documents/ddi-v25/fsd-3307.xml",
					"https://bitbucket.org/cessda/cessda.cmv.core/raw/815a9aa0688300aea56b7ff31bdb99ec9714729d/src/main/resources/demo-documents/ddi-v25/fsd-3307-oaipmh.xml" } )
	void newDocument( String uri )
	{
		Resource resource = newResource( uri );
		assertThat( factory.newDocument( resource ), notNullValue() );
	}

	@Test
	void newDocumentWithNotWellformedDocument() throws IOException
	{
		// given
		Resource resource = newResource( getClass().getResource( "/demo-documents/ddi-v25/ukds-7481-not-wellformed.xml-invalid" ) );
		try ( InputStream inputStream = resource.readInputStream() )
		{
			// when
			Executable executable = () -> factory.newDocument( inputStream );
			// then
			assertThrows( NotDocumentException.class, executable );
		}
	}
}
