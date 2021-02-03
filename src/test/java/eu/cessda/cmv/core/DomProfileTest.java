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

import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.net.URL;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class DomProfileTest
{
	@Test
	@Disabled( "see also DomSemiStructuredDdiProfileTest::toJaxbProfileV0" )
	void test()
	{
		URL url = getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile_cmv.xml" );
		DomProfile profile = new DomProfile( newResource( url ).readInputStream() );
		assertThat( profile.getConstraints(), hasSize( 62 * 3 ) );
	}
}
