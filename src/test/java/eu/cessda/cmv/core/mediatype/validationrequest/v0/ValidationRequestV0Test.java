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

import static org.gesis.commons.resource.Resource.newResource;
import static org.gesis.commons.test.hamcrest.FileMatchers.hasEqualContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;
import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import eu.cessda.cmv.core.ValidationGateName;

class ValidationRequestV0Test
{
	private TestEnv.V14 testEnv;
	private Resource profile;
	private Resource document;
	private ValidationGateName validationGateName;

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
	void writeAndReadXml() throws Exception
	{
		File file = new File( testEnv.newDirectory(), "validation-request.xml" );

		ValidationRequestV0 validationRequest = newValidationRequest();
		// System.out.println( validationRequest.toString() );
		validationRequest.saveAs( file );
		assertThat( file, hasEqualContent( testEnv.findTestResourceByName( "validation-request-UpperCamelCase.xml" ) ) );

		validationRequest = ValidationRequestV0.open( file );
		assertThat( new TextResource( validationRequest.getDocument().toResource() ).toString(), equalTo( document.toString() ) );
		assertThat( new TextResource( validationRequest.getProfile().toResource() ).toString(), equalTo( profile.toString() ) );
		assertThat( validationRequest.getValidationGateName(), equalTo( validationGateName ) );
	}

	@Test
	void writeAndReadJson() throws Exception
	{
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule( new JaxbAnnotationModule() );
		objectMapper.enable( SerializationFeature.INDENT_OUTPUT );
		File file = new File( testEnv.newDirectory(), "validation-request.json" );

		ValidationRequestV0 validationRequest = newValidationRequest();
		String json = objectMapper.writeValueAsString( validationRequest );
		// System.out.println( json );
		testEnv.writeContent( json, file );
		assertThat( file, hasEqualContent( testEnv.findTestResourceByName( "validation-request-lowerCamelCase.json" ) ) );

		validationRequest = objectMapper.readValue( json, ValidationRequestV0.class );
		assertThat( new TextResource( validationRequest.getDocument().toResource() ).toString(), equalTo( document.toString() ) );
		assertThat( new TextResource( validationRequest.getProfile().toResource() ).toString(), equalTo( profile.toString() ) );
		assertThat( validationRequest.getValidationGateName(), equalTo( validationGateName ) );
	}
}
