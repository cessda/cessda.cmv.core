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
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashSet;

import static java.util.Objects.requireNonNull;

public class CessdaControlledVocabularyRepositoryV2 extends AbstractControlledVocabularyRepository
{
	private static final JsonPath codeValuesPath = JsonPath.compile( "$.versions.*.concepts.*.notation" );
	private static final JsonPath descriptiveTermsPath = JsonPath.compile("$.versions.*.concepts.*.title" );

	/**
	 * Construct a new ControlledVocabulary using the given URI.
	 *
	 * @param uri the URI of the controlled vocabulary.
	 * @throws InvalidJsonException if the JSON is invalid.
	 * @throws IOException if an IO error occurs.
	 */
	public CessdaControlledVocabularyRepositoryV2( URI uri ) throws IOException
	{
		super( requireNonNull( uri ) );

		try ( InputStream inputStream = openInputStream( uri.toURL() ) )
		{
			DocumentContext document = JsonPath.parse( inputStream );
			setCodeValues( new HashSet<>( document.read(  codeValuesPath ) ) );
			setDescriptiveTerms( new HashSet<>( document.read(  descriptiveTermsPath ) ) );
		}
	}
}
