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

import org.gesis.commons.resource.ResourceLabelProvider;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ProfileResourceLabelProviderTest
{
	@Test
	void testName()
	{
		URL url = this.getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" );
		assert url != null;
		ResourceLabelProvider labelProvider = new ProfileResourceLabelProvider();
		assertThat( labelProvider.getLabel( newResource( url ) ), equalTo( "CESSDA DATA CATALOGUE (CDC) DDI2.5 PROFILE 1.0.4" ) );
	}
}
