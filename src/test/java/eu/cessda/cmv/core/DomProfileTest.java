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

import org.gesis.commons.xml.XMLDocument;
import org.junit.jupiter.api.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

class DomProfileTest
{
	@Test
	void test() throws IOException, SAXException, NotDocumentException
	{
		try ( InputStream profileStream = getClass().getResourceAsStream( "/profiles/cdc25_profile_cmv.xml" ) )
		{
			DomProfile actualProfile = new DomProfile( profileStream );
			assertThat( actualProfile.getConstraints(), hasSize( 61 + 61 + 27 + 10 + 5 + 24 + 12 ) );

			// Compare to parsing the DDI actualProfile directly
			URL url = getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" );
			assert url != null;


			// should be equal
			InputSource inputSource = new InputSource( url.toExternalForm() );
			eu.cessda.cmv.core.Profile expectedProfile = new DomSemiStructuredDdiProfile( XMLDocument.newBuilder().build( inputSource ) );
			assertThat( actualProfile, equalTo( expectedProfile ) );
		}
	}
}
