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
package eu.cessda.cmv.core.mediatype.validationreport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import eu.cessda.cmv.core.*;
import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.junit.jupiter.api.Test;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.LOWER_CAMEL_CASE;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.UPPER_CAMEL_CASE;
import static eu.cessda.cmv.core.ValidationGateName.STANDARD;
import static eu.cessda.cmv.core.mediatype.validationreport.ValidationReport.SCHEMALOCATION_FILENAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

class ValidationReportTest
{
	private final CessdaMetadataValidatorFactory factory = new CessdaMetadataValidatorFactory();
	private final TestEnv.V11 testEnv;

	ValidationReportTest()
	{
		testEnv = DefaultTestEnv.newInstance( ValidationReportTest.class );
	}

	private ValidationReport newValidationReport() throws IOException, NotDocumentException, URISyntaxException
	{
		URI documentURI = Objects.requireNonNull( getClass().getResource( "/demo-documents/ddi-v25/ukds-7481.xml" ) ).toURI();
		URI profileURI = Objects.requireNonNull( getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" ) ).toURI();

		Document document = factory.newDocument(documentURI);
		Profile profile = factory.newProfile( profileURI );

		ValidationService validationService = factory.newValidationService();
		return validationService.validate( document, profile, STANDARD );
	}

	@Test
	void writeAndReadWithEclipselinkMoxy() throws Exception
	{
		ValidationReport validationReport = newValidationReport();

		// Set the URI to a known value
		validationReport.setDocumentUri( URI.create( "urn:uuid:" + new UUID( 0, 0 ) ) );

        File file = new File( testEnv.newDirectory(), "eclipselink-moxy.xml" );

		// Read and write the report to an XML file
		validationReport.saveAs( file );

		// Compare the report to the expected XML
		assertThat( new StreamSource( file ), isSimilarTo( new StreamSource( testEnv.findTestResourceByName( "eclipselink-moxy.xml" ) ) ).ignoreWhitespace() );
	}

	@Test
	void writeAndReadWithJackson() throws IOException, NotDocumentException, URISyntaxException
	{
		File workingDirectory = testEnv.newDirectory();
		writeAndReadWithJackson( new File( workingDirectory, "jackson.json" ), LOWER_CAMEL_CASE, new ObjectMapper() );
		writeAndReadWithJackson( new File( workingDirectory, "jackson.xml" ), UPPER_CAMEL_CASE, new XmlMapper() );
	}

	private void writeAndReadWithJackson( File file, PropertyNamingStrategy propertyNamingStrategy, ObjectMapper objectMapper ) throws IOException, NotDocumentException, URISyntaxException
	{
		objectMapper.setPropertyNamingStrategy( propertyNamingStrategy );
		objectMapper.registerModule( new JaxbAnnotationModule() );

		ValidationReport validationReport = newValidationReport();

		// Set the URI to a known value
		validationReport.setDocumentUri( URI.create( "urn:uuid:" + new UUID( 0, 0 ) ) );

        // Serialise the result to a string
		String content = objectMapper.writeValueAsString( validationReport );

		// Parse the string into a JSON tree, and compare with the reference file
		// Direct string comparisons are unreliable (e.g. line endings, differences in serialisation)
		JsonNode actualTree = objectMapper.readTree( content );
		JsonNode expectedTree = objectMapper.readTree( testEnv.findTestResourceByName( file.getName() ) );
		assertThat( actualTree, equalTo( expectedTree ) );
	}

	@Test
	void generateSchema()
    {
		File expectedFile = testEnv.findTestResourceByName( SCHEMALOCATION_FILENAME );
		File actualFile = new File( testEnv.newDirectory(), SCHEMALOCATION_FILENAME );

		ValidationReport.generateSchema( actualFile );

		// Compare the generated file to the expected file
		assertThat( new StreamSource( actualFile ), isSimilarTo( new StreamSource( expectedFile ) ).ignoreWhitespace() );
	}
}
