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
import static eu.cessda.cmv.core.mediatype.validationrequest.v0.ValidationRequestV0.SCHEMALOCATION_FILENAME;
import static org.gesis.commons.resource.Resource.newResource;
import static org.gesis.commons.test.hamcrest.FileMatchers.hasEqualContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

@TestInstance( TestInstance.Lifecycle.PER_CLASS )
class ValidationRequestV0Test
{
	private final TestEnv.V14 testEnv;
	private final Resource profile;
	private final Resource document;
	private final ValidationGateName validationGateName;
	private final Set<String> constraints;
	;

	ValidationRequestV0Test()
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
		testEnv = DefaultTestEnv.newInstance( ValidationRequestV0Test.class );
	}

	private ValidationRequestProvider[] newValidationRequest()
	{
		ValidationRequestV0 validationRequestWithConstraints = new ValidationRequestV0();
		validationRequestWithConstraints.setDocument( document.getUri() );
		validationRequestWithConstraints.setProfile( profile.toString() );
		validationRequestWithConstraints.setConstraints( constraints );

		ValidationRequestV0 validationRequestWithGateName = new ValidationRequestV0();
		validationRequestWithGateName.setDocument( document.getUri() );
		validationRequestWithGateName.setProfile( profile.toString() );
		validationRequestWithGateName.setValidationGateName( validationGateName );

		return new ValidationRequestProvider[]{
				new ValidationRequestProvider( validationRequestWithConstraints, request ->
				{
					assertThat( new TextResource( request.getDocument()
							.toResource() ).toString(), equalTo( document.toString() ) );
					assertThat( new TextResource( request.getProfile()
							.toResource() ).toString(), equalTo( profile.toString() ) );
					assertThat( request.getConstraints(), contains( constraints.toArray() ) );
				} ),
				new ValidationRequestProvider( validationRequestWithGateName, request ->
				{
					assertThat( new TextResource( request.getDocument()
							.toResource() ).toString(), equalTo( document.toString() ) );
					assertThat( new TextResource( request.getProfile()
							.toResource() ).toString(), equalTo( profile.toString() ) );
					assertThat( request.getValidationGateName(), equalTo( validationGateName ) );
				} )
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

		ValidationRequestV0 validationRequest = ValidationRequestV0.open( file );
		validationRequestProvider.validator.accept( validationRequest );
	}

	Stream<Arguments> writeAndReadWithJackson()
	{
		return Stream.of( newValidationRequest() )
				.flatMap( validationRequestProvider ->
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

		ValidationRequestV0 validationRequest = objectMapper.readValue( content, ValidationRequestV0.class );
		validationRequestProvider.validator.accept( validationRequest );
	}

	@Test
	@Disabled( "Schema generation not correct because of DocumentV0Adapter usage" )
	void generateSchema()
	{
		File actualFile = new File( testEnv.newDirectory(), SCHEMALOCATION_FILENAME );
		ValidationRequestV0.generateSchema( actualFile );

		File expectedFile = testEnv.findTestResourceByName( SCHEMALOCATION_FILENAME );
		assertThat( actualFile, hasEqualContent( expectedFile ) );
	}

	private static class ValidationRequestProvider
	{
		private final ValidationRequestV0 request;
		private final Consumer<ValidationRequestV0> validator;

		ValidationRequestProvider( ValidationRequestV0 request, Consumer<ValidationRequestV0> validator )
		{
			this.request = request;
			this.validator = validator;
		}
	}
}
