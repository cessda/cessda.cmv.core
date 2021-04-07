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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;
import org.junit.jupiter.api.Test;

class ValidationRequestV0Test
{
	@Test
	void writeAndRead() throws Exception
	{
		File file = new File( "target/validation-request.xml" );
		String baseUrl = "https://bitbucket.org/cessda/cessda.cmv.core/raw/1a01a5e7ede385699e169a56ab9e700de716778a/src/main/resources/demo-documents/ddi-v25";
		Resource.V10 profileResource = Resource.newResource( baseUrl + "/cdc25_profile.xml" );
		Resource.V10 documentResource = Resource.newResource( baseUrl + "/gesis-5300.xml" );

		// given
		ValidationRequestV0 validationRequest = new ValidationRequestV0();
		DocumentV0 document = new DocumentV0();
		document.setUri( documentResource.getUri() );
		document.setContent( new TextResource( documentResource ).toString() );
		validationRequest.setDocument( document );
		DocumentV0 profile = new DocumentV0();
		profile.setUri( profileResource.getUri() );
		profile.setContent( new TextResource( profileResource ).toString() );
		validationRequest.setProfile( profile );
		validationRequest.saveAs( file );

		// when
		validationRequest = ValidationRequestV0.open( file );

		// then
		assertThat( validationRequest.getDocument().getContent(), equalTo( new TextResource( documentResource ).toString() ) );
		assertThat( validationRequest.getProfile().getContent(), equalTo( new TextResource( profileResource ).toString() ) );
	}
}
