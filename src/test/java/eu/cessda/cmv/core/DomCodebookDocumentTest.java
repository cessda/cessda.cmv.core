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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.URL;

import org.gesis.commons.resource.io.DdiInputStream;
import org.gesis.commons.xml.XmlNotWellformedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class DomCodebookDocumentTest
{
	@Test
	void constructWithNotWellformedDocument() throws IOException
	{
		// given
		URL url = getClass().getResource( "/demo-documents/ddi-v25/ukds-7481-not-wellformed.xml-invalid" );
		try ( DdiInputStream documentInputStream = new DdiInputStream( newResource( url ).readInputStream() ) )
		{
			// when
			Executable executable = () -> new DomCodebookDocument( documentInputStream );
			// then
			assertThrows( XmlNotWellformedException.class, executable );
		}
	}
}
