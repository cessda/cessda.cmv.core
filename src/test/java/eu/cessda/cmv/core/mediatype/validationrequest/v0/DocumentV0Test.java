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
import static org.hamcrest.Matchers.hasSize;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;
import org.junit.jupiter.api.Test;

class DocumentV0Test
{
	@Test
	void validate()
	{
		DocumentV0 document;
		String baseUrl = "https://bitbucket.org/cessda/cessda.cmv.core/raw/1a01a5e7ede385699e169a56ab9e700de716778a/src/main/resources/demo-documents/ddi-v25";
		Resource.V10 resource = newResource( baseUrl + "/gesis-5300.xml" );

		document = new DocumentV0();
		assertThat( document.validate(), hasSize( 1 ) );

		document = new DocumentV0();
		document.setUri( resource.getUri() );
		assertThat( document.validate(), hasSize( 0 ) );

		document = new DocumentV0();
		document.setContent( new TextResource( resource ).toString() );
		assertThat( document.validate(), hasSize( 0 ) );

		document = new DocumentV0();
		document.setUri( resource.getUri() );
		document.setContent( new TextResource( resource ).toString() );
		assertThat( document.validate(), hasSize( 0 ) );

		document = new DocumentV0();
		document.setUri( resource.getUri() );
		document.setContent( new TextResource( newResource( baseUrl + "/cdc25_profile.xml" ) ).toString() );
		assertThat( document.validate(), hasSize( 1 ) );
	}
}
