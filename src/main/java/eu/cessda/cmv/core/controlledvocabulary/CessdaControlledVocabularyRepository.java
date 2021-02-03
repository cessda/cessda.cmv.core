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

import static java.util.Objects.requireNonNull;
import static org.gesis.commons.resource.Resource.newResource;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class CessdaControlledVocabularyRepository extends AbstractControlledVocabularyRepository
{
	public CessdaControlledVocabularyRepository( URI uri )
	{
		this( (Resource) newResource( requireNonNull( uri ) ) );
	}

	public CessdaControlledVocabularyRepository( Resource resource )
	{
		requireNonNull( resource );
		setUri( resource.getUri() );
		Object document = Configuration.defaultConfiguration().jsonProvider()
				.parse( new TextResource( resource ).toString() );
		List<String> list = JsonPath.read( document, "$.conceptAsMap.*.notation" );
		setCodeValues( list.stream().collect( Collectors.toSet() ) );
		list = JsonPath.read( document, "$.conceptAsMap.*.title" );
		setDescriptiveTerms( list.stream().collect( Collectors.toSet() ) );
	}
}
