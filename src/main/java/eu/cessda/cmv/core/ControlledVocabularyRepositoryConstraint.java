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

import javax.xml.xpath.XPathExpressionException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class ControlledVocabularyRepositoryConstraint extends NodeConstraint
{
	private static final Logger LOGGER = LoggerFactory.getLogger( ControlledVocabularyRepositoryConstraint.class );

	private final String type;
	private final URI uri;

	ControlledVocabularyRepositoryConstraint( String locationPath, String repositoryType, URI repositoryUri )
	{
        super(locationPath);
		this.type = repositoryType;
		this.uri = repositoryUri;
	}

	@SuppressWarnings( "OverlyBroadCatchBlock" )
	@Override
	public List<Validator> newNodeValidators( Document document ) throws XPathExpressionException
	{
		// Attempt to load a cached repository, fall back to creating a new repository if not present
		ControlledVocabularyRepository repository = document.findControlledVocabularyRepository(uri);

		if ( repository == null )
		{
			try
			{
				Class<?> clazz = Class.forName( type );
				repository = (ControlledVocabularyRepository) clazz
					.getDeclaredConstructor( URI.class )
					.newInstance( uri );
			}
			catch ( ReflectiveOperationException e )
			{
				LOGGER.warn( "Instancing {} failed, falling back to empty controlled vocabulary repository", type, e );
				repository = EmptyControlledVocabularyRepository.INSTANCE;
			}

			document.register( repository );
		}

		int count = 0;
		for ( Node node : document.getNodes( getLocationPath() ) )
		{
			if ( node.getTextContent().contentEquals( uri.toString() ) )
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

	@Override
	public boolean equals( Object o )
	{
		if ( this == o ) return true;
		if ( o == null || getClass() != o.getClass() ) return false;
		if ( !super.equals( o ) ) return false;
		ControlledVocabularyRepositoryConstraint that = (ControlledVocabularyRepositoryConstraint) o;
		return Objects.equals( type, that.type ) && Objects.equals( uri, that.uri );
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( super.hashCode(), type, uri );
	}
}
