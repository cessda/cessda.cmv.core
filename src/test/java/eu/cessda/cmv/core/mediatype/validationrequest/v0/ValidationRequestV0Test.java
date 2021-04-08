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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.io.File;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;
import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.junit.jupiter.api.Test;

import eu.cessda.cmv.core.ValidationGateName;

class ValidationRequestV0Test
{
	private TestEnv.V14 testEnv;

	ValidationRequestV0Test()
	{
		testEnv = DefaultTestEnv.newInstance( ValidationRequestV0Test.class );
	}

	@Test
	void writeAndRead() throws Exception
	{
		File file = new File( testEnv.newDirectory(), "validation-request.xml" );
		String baseUrl = "https://bitbucket.org/cessda/cessda.cmv.core/raw/1a01a5e7ede385699e169a56ab9e700de716778a/src/main/resources/demo-documents/ddi-v25";
		Resource profile = new TextResource( newResource( baseUrl + "/cdc25_profile.xml" ) );
		Resource document = new TextResource( newResource( baseUrl + "/gesis-5300.xml" ) );
		ValidationGateName validationGateName = ValidationGateName.BASIC;

		// given
		ValidationRequestV0 validationRequest = new ValidationRequestV0();
		validationRequest.setDocument( document.getUri() );
		validationRequest.setProfile( profile.toString() );
		validationRequest.setValidationGateName( validationGateName );
		assertThat( validationRequest.validate(), hasSize( 0 ) );
		validationRequest.saveAs( file );

		// when
		validationRequest = ValidationRequestV0.open( file );

		// then
		assertThat( new TextResource( validationRequest.getDocument().toResource() ).toString(), equalTo( document.toString() ) );
		assertThat( new TextResource( validationRequest.getProfile().toResource() ).toString(), equalTo( profile.toString() ) );
		assertThat( validationRequest.getValidationGateName(), equalTo( validationGateName ) );
	}
}
