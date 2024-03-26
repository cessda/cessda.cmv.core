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

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

class ControlledVocabularyRepositoryConstraint implements Constraint
{
	private static final Logger LOGGER = LoggerFactory.getLogger( ControlledVocabularyRepositoryConstraint.class );

	private final String locationPath;
	private final String type;
	private final String uri;

	public ControlledVocabularyRepositoryConstraint(
			String locationPath,
			String repositoryType,
			String repositoryUri )
	{
		this.locationPath = locationPath;
		this.type = repositoryType;
		this.uri = repositoryUri;
	}

	@SuppressWarnings( "OverlyBroadCatchBlock" )
	@Override
	public List<Validator> newValidators( Document document )
	{
		ControlledVocabularyRepository repository;
		try
		{
			Class<?> clazz = Class.forName( type );
			repository = (ControlledVocabularyRepository) clazz
				.getDeclaredConstructor( URI.class )
				.newInstance( new URI(uri) );
		}
		catch ( ReflectiveOperationException | URISyntaxException e )
		{
			LOGGER.warn( "Instancing {} failed, falling back to empty controlled vocabulary repository", type, e );
			repository = EmptyControlledVocabularyRepository.INSTANCE;
		}

		document.register( uri, repository );

		int count = 0;
		for ( Node node : document.getNodes( locationPath ) )
		{
			if ( node.getTextContent().contentEquals( uri ) )
			{
				count++;
			}
		}

		if ( count > 0 )
		{
			return Collections.singletonList( new ControlledVocabularyRepositoryValidator( repository ) );
		}
		else
		{
			return Collections.emptyList();
		}
	}
}
