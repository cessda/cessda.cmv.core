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

import org.gesis.commons.resource.Resource;
import org.gesis.commons.xml.XMLDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CessdaMetadataValidatorFactoryTest
{
	private final CessdaMetadataValidatorFactory factory;

	CessdaMetadataValidatorFactoryTest()
	{
		factory = new CessdaMetadataValidatorFactory();
	}

	@Test
	void newDomDocument() throws IOException, SAXException, XPathExpressionException
	{
		URL resourceUrl = this.getClass().getResource( "/cmv-profile-ddi-v32.xml" );
		assert resourceUrl != null;
		InputSource inputSource = new InputSource( resourceUrl.toExternalForm() );
		XMLDocument document = XMLDocument.newBuilder().namespaceAware( true ).build( inputSource );
		assertThat( document.selectNode( "/pr:DDIProfile" ), notNullValue() );
	}

	@ParameterizedTest
	@ValueSource( strings = { "/demo-documents/ddi-v25/fsd-3307.xml", "/demo-documents/ddi-v25/fsd-3307-oaipmh.xml", "/demo-documents/ddi-v32/gesis-5300.xml"} )
	void newDocument( String uri ) throws IOException, NotDocumentException
	{
		URL resourceUrl = this.getClass().getResource( uri );
		assert resourceUrl != null;
		assertThat( resourceUrl, notNullValue() );
	}

	@Test
	void newDocumentWithNotWellFormedDocument() throws IOException
	{
		// given
		URL resourceUrl = getClass().getResource( "/demo-documents/ddi-v25/ukds-7481-not-wellformed.xml-invalid" );
		assert resourceUrl != null;
		Resource resource = newResource( resourceUrl );
		try ( InputStream inputStream = resource.readInputStream() )
		{
			// when
			Executable executable = () -> factory.newDocument( inputStream );
			// then
			assertThrows( NotDocumentException.class, executable );
		}
	}
}
