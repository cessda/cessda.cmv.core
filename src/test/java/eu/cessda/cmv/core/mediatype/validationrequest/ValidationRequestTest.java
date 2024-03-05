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
package eu.cessda.cmv.core.mediatype.validationrequest;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import eu.cessda.cmv.core.CessdaMetadataValidatorFactory;
import eu.cessda.cmv.core.ValidationGateName;
import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;
import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.net.URL;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.LOWER_CAMEL_CASE;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.UPPER_CAMEL_CASE;
import static eu.cessda.cmv.core.mediatype.validationrequest.ValidationRequest.SCHEMALOCATION_FILENAME;
import static org.gesis.commons.resource.Resource.newResource;
import static org.gesis.commons.test.hamcrest.FileMatchers.hasEqualContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance( TestInstance.Lifecycle.PER_CLASS )
class ValidationRequestTest
{
	private final TestEnv.V14 testEnv;
	private final Resource profile;
	private final Resource document;
	private final ValidationGateName validationGateName;
	private final Set<String> constraints;

	ValidationRequestTest()
	{
		String baseUrl = "/demo-documents/ddi-v25";

		URL profileResource = this.getClass().getResource( baseUrl + "/cdc25_profile.xml" );
		URL documentResource = this.getClass().getResource( baseUrl + "/gesis-5300.xml" );

		assert profileResource != null;
		assert documentResource != null;

		profile = new TextResource( newResource( profileResource ) );
		document = new TextResource( newResource( documentResource ) );
		validationGateName = ValidationGateName.BASIC;
		constraints = CessdaMetadataValidatorFactory.getConstraints();
		testEnv = DefaultTestEnv.newInstance( ValidationRequestTest.class );
	}

	private ValidationRequestProvider[] newValidationRequest()
	{
		ValidationRequest validationRequestWithConstraints = new ValidationRequest();
		validationRequestWithConstraints.setDocument( document.getUri() );
		validationRequestWithConstraints.setProfile( profile.toString() );
		validationRequestWithConstraints.setConstraints( constraints );

		ValidationRequest validationRequestWithGateName = new ValidationRequest();
		validationRequestWithGateName.setDocument( document.getUri() );
		validationRequestWithGateName.setProfile( profile.toString() );
		validationRequestWithGateName.setValidationGateName( validationGateName );

		return new ValidationRequestProvider[]{
				new ValidationRequestProvider( validationRequestWithConstraints, request ->
                        assertThat( request.getConstraints(), contains( constraints.toArray() ) )
				),
				new ValidationRequestProvider( validationRequestWithGateName, request ->
                        assertThat( request.getValidationGateName(), equalTo( validationGateName ) )
				)
		};
	}

	@ParameterizedTest
	@MethodSource( "newValidationRequest" )
	@SuppressWarnings( "java:S2699" )
		// Assertions are provided as part of the validationRequestProvider parameter
	void writeAndReadWithEclipselinkMoxy( ValidationRequestProvider validationRequestProvider ) throws Exception
	{
		File file = new File( testEnv.newDirectory(), "eclipselink-moxy.xml" );

		validationRequestProvider.request.saveAs( file );

		ValidationRequest validationRequest = ValidationRequest.open( file );
		validationRequestProvider.validator.accept( validationRequest );
	}

	Stream<Arguments> writeAndReadWithJackson()
	{
		return Stream.of( newValidationRequest() ).flatMap( validationRequestProvider ->
				Stream.of(
						Arguments.of( validationRequestProvider, LOWER_CAMEL_CASE, new ObjectMapper() ),
						Arguments.of( validationRequestProvider, UPPER_CAMEL_CASE, new XmlMapper() )
				)
		);
	}

	@ParameterizedTest
	@MethodSource
	@SuppressWarnings( "java:S2699" )
		// Assertions are provided as part of the validationRequestProvider parameter
	void writeAndReadWithJackson( ValidationRequestProvider validationRequestProvider, PropertyNamingStrategy propertyNamingStrategy, ObjectMapper objectMapper ) throws JsonProcessingException
	{
		objectMapper.setPropertyNamingStrategy( propertyNamingStrategy );
		objectMapper.registerModule( new JaxbAnnotationModule() );
		objectMapper.setSerializationInclusion( Include.NON_NULL );
		objectMapper.enable( SerializationFeature.INDENT_OUTPUT );

		String content = objectMapper.writeValueAsString( validationRequestProvider.request );

		ValidationRequest validationRequest = objectMapper.readValue( content, ValidationRequest.class );
		validationRequestProvider.validator.accept( validationRequest );
	}

	@Test
	void shouldValidateRequests()
	{
		// Generate an invalid request
		ValidationRequest invalidRequest = new ValidationRequest();

		// Assert that all elements are missing
		assertThat( invalidRequest.validate(), containsInAnyOrder(
			"Document is missing",
			"Profile is missing",
			"Validation gate or constraints is missing"
		) );

		ValidationRequestProvider[] validRequest = newValidationRequest();
		for ( ValidationRequestProvider validationRequest : validRequest )
		{
			assertThat( validationRequest.request.validate(), is( empty() ) );
		}
	}

	@Test
	void testEquality()
	{
		// Generate two identical empty requests
		ValidationRequest firstEmptyRequest = new ValidationRequest();
		ValidationRequest secondEmptyRequest = new ValidationRequest();

		// Should be equal
		assertThat( firstEmptyRequest, equalTo( secondEmptyRequest ) );

		// Generate two identical populated requests
		ValidationRequest firstPopulatedRequest = new ValidationRequest();
		firstPopulatedRequest.setDocument( document.getUri() );
		firstPopulatedRequest.setProfile( profile.toString() );
		firstPopulatedRequest.setConstraints( constraints );

		ValidationRequest secondPopulatedRequest = new ValidationRequest();
		secondPopulatedRequest.setDocument( document.getUri() );
		secondPopulatedRequest.setProfile( profile.toString() );
		secondPopulatedRequest.setConstraints( constraints );

		// Should be equal
		assertThat( firstPopulatedRequest, equalTo( firstPopulatedRequest ) );
		assertThat( firstPopulatedRequest, equalTo( secondPopulatedRequest ) );

		// A comparison between a populated request and an empty request should never be equal
		assertThat( firstPopulatedRequest, not( equalTo( secondEmptyRequest ) ) );

		// A comparison between dissimilar types should not be equal
		assertThat( firstPopulatedRequest, not( equalTo( null ) ) );
		assertThat( firstPopulatedRequest, not( equalTo( new Object() ) ) );
	}

	@Test
	@Disabled( "Schema generation not correct because of DocumentV0Adapter usage" )
	void generateSchema()
	{
		File actualFile = new File( testEnv.newDirectory(), SCHEMALOCATION_FILENAME );
		ValidationRequest.generateSchema( actualFile );

		File expectedFile = testEnv.findTestResourceByName( SCHEMALOCATION_FILENAME );
		assertThat( actualFile, hasEqualContent( expectedFile ) );
	}

	private static class ValidationRequestProvider
	{
		private final ValidationRequest request;
		private final Consumer<ValidationRequest> validator;

		ValidationRequestProvider( ValidationRequest request, Consumer<ValidationRequest> validator )
		{
			this.request = request;
			this.validator = validator;
		}
	}
}
