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
package eu.cessda.cmv.core.controlledvocabulary;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashSet;

public class CessdaControlledVocabularyRepository extends AbstractControlledVocabularyRepository
{
	public CessdaControlledVocabularyRepository( URI uri ) throws IOException
	{
		super( uri );
		try ( InputStream repositoryStream = uri.toURL().openStream() )
		{
			DocumentContext document = JsonPath.parse( repositoryStream );
			setCodeValues( new HashSet<>( document.read( "$.conceptAsMap.*.notation" ) ) );
			setDescriptiveTerms( new HashSet<>( document.read( "$.conceptAsMap.*.title" ) ) );
		}
	}
}
