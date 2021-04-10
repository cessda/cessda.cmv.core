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
package eu.cessda.cmv.core.mediatype.validationreport.v0;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.LOWER_CAMEL_CASE;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.UPPER_CAMEL_CASE;
import static eu.cessda.cmv.core.ValidationGateName.STANDARD;
import static eu.cessda.cmv.core.mediatype.validationreport.v0.ValidationReportV0.SCHEMALOCATION_FILENAME;
import static org.gesis.commons.resource.Resource.newResource;
import static org.gesis.commons.test.hamcrest.FileMatchers.hasEqualContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import eu.cessda.cmv.core.CessdaMetadataValidatorFactory;
import eu.cessda.cmv.core.ValidationService;

@Disabled( "Only on CESSDA CI, test cases fail, see https://jenkins.cessda.eu/job/cessda.cmv.core/job/master/183/console" )
class ValidationReportV0Test
{
	private static final Logger LOGGER = LoggerFactory.getLogger( ValidationReportV0Test.class );

	private TestEnv.V11 testEnv;

	ValidationReportV0Test()
	{
		testEnv = DefaultTestEnv.newInstance( ValidationReportV0Test.class );
	}

	private ValidationReportV0 newValidationReportV0()
	{
		try
		{
			Resource document = newResource( getClass().getResource( "/demo-documents/ddi-v25/ukds-7481.xml" ).toURI() );
			Resource profile = newResource( getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" ).toURI() );
			ValidationService.V10 validationService = new CessdaMetadataValidatorFactory().newValidationService();
			return validationService.validate( document, profile, STANDARD );
		}
		catch (URISyntaxException e)
		{
			throw new IllegalArgumentException( e );
		}
	}

	@Test
	void writeAndReadWithEclipselinkMoxy() throws Exception
	{
		ValidationReportV0 validationReport = newValidationReportV0();
		List<ConstraintViolationV0> constraintViolations = validationReport.getConstraintViolations();

		File file = new File( testEnv.newDirectory(), "eclipselink-moxy.xml" );
		validationReport.saveAs( file );
		validationReport = ValidationReportV0.open( file );
		assertThat( validationReport.getConstraintViolations().size(), equalTo( constraintViolations.size() ) );
		assertThat( file, hasEqualContent( testEnv.findTestResourceByName( file.getName() ) ) );

		LOGGER.warn( "Print out files to see possible diffs" );
		LOGGER.warn( "" );
		LOGGER.warn( "" );
		LOGGER.warn( testEnv.findTestResourceByName( file.getName() ).getAbsolutePath() );
		LOGGER.warn( testEnv.readContent( testEnv.findTestResourceByName( file.getName() ) ) );
		LOGGER.warn( "" );
		LOGGER.warn( file.getAbsolutePath() );
		LOGGER.warn( testEnv.readContent( file ) );
	}

	@Test
	void writeAndReadWithJackson()
	{
		File workingDirectory = testEnv.newDirectory();
		writeAndReadWithJackson( new File( workingDirectory, "jackson.json" ), LOWER_CAMEL_CASE, new ObjectMapper() );
		writeAndReadWithJackson( new File( workingDirectory, "jackson.xml" ), UPPER_CAMEL_CASE, new XmlMapper() );
	}

	private void writeAndReadWithJackson( File file, PropertyNamingStrategy propertyNamingStrategy, ObjectMapper objectMapper )
	{
		try
		{
			objectMapper.setPropertyNamingStrategy( propertyNamingStrategy );
			objectMapper.registerModule( new JaxbAnnotationModule() );
			objectMapper.enable( SerializationFeature.INDENT_OUTPUT );
			// objectMapper.setSerializationInclusion( Include.NON_NULL );

			ValidationReportV0 validationReport = newValidationReportV0();
			List<ConstraintViolationV0> constraintViolations = validationReport.getConstraintViolations();
			String content = objectMapper.writeValueAsString( validationReport );
			testEnv.writeContent( content, file );
			assertThat( file, hasEqualContent( testEnv.findTestResourceByName( file.getName() ) ) );

			validationReport = objectMapper.readValue( content, ValidationReportV0.class );
			assertThat( validationReport.getConstraintViolations().size(), equalTo( constraintViolations.size() ) );
		}
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
			Assertions.fail( e.getMessage() );
		}
	}

	@Test
	void generateSchema()
	{
		File expectedFile = testEnv.findTestResourceByName( SCHEMALOCATION_FILENAME );
		File actualFile = new File( testEnv.newDirectory(), SCHEMALOCATION_FILENAME );

		ValidationReportV0.generateSchema( actualFile );

		assertThat( actualFile, hasEqualContent( expectedFile ) );
	}
}
