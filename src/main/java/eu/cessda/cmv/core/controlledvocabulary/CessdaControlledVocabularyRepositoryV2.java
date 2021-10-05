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

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;
import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.gesis.commons.resource.Resource.newResource;
import static org.springframework.http.HttpMethod.GET;

public class CessdaControlledVocabularyRepositoryV2 extends AbstractControlledVocabularyRepository
{
	public CessdaControlledVocabularyRepositoryV2( URI uri )
	{
		this( (Resource) newResource( requireNonNull( uri ) ) );
	}

	public CessdaControlledVocabularyRepositoryV2( Resource resource )
	{
		requireNonNull( resource );
		setUri( resource.getUri() );
		Object document;
		if ( resource.getUri().getScheme().startsWith( "http" ) )
		{
			try
			{
				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept( Collections.singletonList( MediaType.APPLICATION_JSON ) );
				HttpEntity<String> entity = new HttpEntity<>( "accept", headers );
				ResponseEntity<String> responseEntity = restTemplate.exchange( getUri(), GET, entity, String.class );
				document = Configuration.defaultConfiguration().jsonProvider().parse( responseEntity.getBody() );
			}
			catch ( RestClientException | InvalidJsonException e )
			{
				throw new IllegalArgumentException( "Resource not found", e );
			}
		}
		else
		{
			document = Configuration.defaultConfiguration().jsonProvider()
					.parse( new TextResource( resource ).toString() );
		}
		List<String> list = JsonPath.read( document, "$.versions.*.concepts.*.notation" );
		setCodeValues( new HashSet<>( list ) );
		list = JsonPath.read( document, "$.versions.*.concepts.*.title" );
		setDescriptiveTerms( new HashSet<>( list ) );
	}
}
