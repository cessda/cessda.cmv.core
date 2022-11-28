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
package eu.cessda.cmv.core.mediatype.validationrequest.v0;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import eu.cessda.cmv.core.ValidationGateName;
import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;
import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.LOWER_CAMEL_CASE;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.UPPER_CAMEL_CASE;
import static eu.cessda.cmv.core.mediatype.validationrequest.v0.ValidationRequestV0.SCHEMALOCATION_FILENAME;
import static org.gesis.commons.resource.Resource.newResource;
import static org.gesis.commons.test.hamcrest.FileMatchers.hasEqualContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ValidationRequestV0Test
{
	private final TestEnv.V14 testEnv;
	private final Resource profile;
	private final Resource document;
	private final ValidationGateName validationGateName;

	ValidationRequestV0Test()
	{
		String baseUrl = "https://bitbucket.org/cessda/cessda.cmv.core/raw/1a01a5e7ede385699e169a56ab9e700de716778a/src/main/resources/demo-documents/ddi-v25";
		profile = new TextResource( newResource( baseUrl + "/cdc25_profile.xml" ) );
		document = new TextResource( newResource( baseUrl + "/gesis-5300.xml" ) );
		validationGateName = ValidationGateName.BASIC;
		testEnv = DefaultTestEnv.newInstance( ValidationRequestV0Test.class );
	}

	private ValidationRequestV0 newValidationRequest()
	{
		ValidationRequestV0 validationRequest = new ValidationRequestV0();
		validationRequest.setDocument( document.getUri() );
		validationRequest.setProfile( profile.toString() );
		validationRequest.setValidationGateName( validationGateName );
		return validationRequest;
	}

	@Test
	void writeAndReadWithEclipselinkMoxy() throws Exception
	{
		File file = new File( testEnv.newDirectory(), "eclipselink-moxy.xml" );

		ValidationRequestV0 validationRequest = newValidationRequest();
		// System.out.println( validationRequest.toString() );
		validationRequest.saveAs( file );
		assertThat( file, hasEqualContent( testEnv.findTestResourceByName( file.getName() ) ) );

		validationRequest = ValidationRequestV0.open( file );
		assertThat( new TextResource( validationRequest.getDocument().toResource() ).toString(), equalTo( document.toString() ) );
		assertThat( new TextResource( validationRequest.getProfile().toResource() ).toString(), equalTo( profile.toString() ) );
		assertThat( validationRequest.getValidationGateName(), equalTo( validationGateName ) );
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
			objectMapper.setSerializationInclusion( Include.NON_NULL );
			objectMapper.enable( SerializationFeature.INDENT_OUTPUT );

			ValidationRequestV0 validationRequest = newValidationRequest();
			String content = objectMapper.writeValueAsString( validationRequest );
			testEnv.writeContent( content, file );
			assertThat( file, hasEqualContent( testEnv.findTestResourceByName( file.getName() ) ) );

			validationRequest = objectMapper.readValue( content, ValidationRequestV0.class );
			assertThat( new TextResource( validationRequest.getDocument().toResource() ).toString(), equalTo( document.toString() ) );
			assertThat( new TextResource( validationRequest.getProfile().toResource() ).toString(), equalTo( profile.toString() ) );
			assertThat( validationRequest.getValidationGateName(), equalTo( validationGateName ) );
		}
		catch (JsonProcessingException e)
		{
			e.printStackTrace();
			Assertions.fail( e.getMessage() );
		}
	}

	@Test
	@Disabled( "Schema generation not correct because of DocumentV0Adapter usage" )
	void generateSchema() throws IOException
	{
		File actualFile = new File( testEnv.newDirectory(), SCHEMALOCATION_FILENAME );
		ValidationRequestV0.generateSchema( actualFile );

		File expectedFile = testEnv.findTestResourceByName( SCHEMALOCATION_FILENAME );
		assertThat( actualFile, hasEqualContent( expectedFile ) );
	}
}
