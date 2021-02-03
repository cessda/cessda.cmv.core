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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;

import org.gesis.commons.xml.DomDocument;
import org.junit.jupiter.api.Test;

class CessdaMetadataValidatorFactoryTest
{
	private CessdaMetadataValidatorFactory factory;

	CessdaMetadataValidatorFactoryTest()
	{
		factory = new CessdaMetadataValidatorFactory();
	}

	@Test
	void newDomDocument()
	{
		File file = new File( "src/main/resources/cmv-profile-ddi-v32.xml" );
		DomDocument.V11 document = factory.newDomDocument( file );
		assertThat( document.selectNode( "/pr:DDIProfile" ), notNullValue() );
	}
}
