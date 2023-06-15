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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class ControlledVocabularyRepositoryProxy implements ControlledVocabularyRepository.V11
{
	private static final Logger LOGGER = LoggerFactory.getLogger( ControlledVocabularyRepositoryProxy.class );

	private final URI uri;
	private final String canonicalName;
	private ControlledVocabularyRepository.V11 repository;
	private final boolean isTolerant;

	/**
	 * Creates a ControlledVocabularyRepositoryProxy that proxies the given {@link ControlledVocabularyRepository.V11}.
	 * The {@link ControlledVocabularyRepository.V11} must have a constructor that takes a {@link URI} parameter.
	 * <p>
	 * The proxy will not be tolerant of instantiation errors which will result in {@link IllegalStateException}
	 * being thrown on the invocation of findCodeValues() or findDescriptiveTerms().
	 *
	 * @param canonicalName the canonical name of the class to proxy.
	 * @param uri the URI of the controlled vocabulary.
	 * @throws IllegalArgumentException if the URI violates RFC 2396.
	 */
	public ControlledVocabularyRepositoryProxy( String canonicalName, String uri )
	{
		this( canonicalName, uri, false );
	}

	/**
	 * Creates a ControlledVocabularyRepositoryProxy that proxies the given {@link ControlledVocabularyRepository.V11}.
	 * The {@link ControlledVocabularyRepository.V11} must have a constructor that takes a {@link URI} parameter.
	 *
	 * @param canonicalName the canonical name of the class to proxy.
	 * @param uri the URI of the controlled vocabulary.
	 * @param isTolerant whether the proxy should fall back to an {@link EmptyControlledVocabularyRepository}
	 *                   if the desired {@link ControlledVocabularyRepository.V11} cannot be instanced.
	 * @throws IllegalArgumentException if the URI violates RFC 2396.
	 */
	public ControlledVocabularyRepositoryProxy( String canonicalName, String uri, boolean isTolerant )
	{
		this( canonicalName, URI.create(uri), isTolerant );
	}

	/**
	 * Creates a ControlledVocabularyRepositoryProxy that proxies the given {@link ControlledVocabularyRepository.V11}.
	 * The {@link ControlledVocabularyRepository.V11} must have a constructor that takes a {@link URI} parameter.
	 *
	 * @param canonicalName the canonical name of the class to proxy.
	 * @param uri the URI of the controlled vocabulary.
	 * @param isTolerant whether the proxy should fall back to an {@link EmptyControlledVocabularyRepository}
	 *                   if the desired {@link ControlledVocabularyRepository.V11} cannot be instanced.
	 */
	public ControlledVocabularyRepositoryProxy( String canonicalName, URI uri, boolean isTolerant )
	{
		requireNonNull( canonicalName );
		requireNonNull( uri );

		this.canonicalName = canonicalName;
		this.uri = uri;
		this.isTolerant = isTolerant;
	}

	/**
	 * Instance the ControlledVocabularyRepository.
	 *
	 * @throws IllegalStateException if an exception occurs when instancing.
	 */
	@SuppressWarnings( "OverlyBroadCatchBlock" )
	private void unproxy()
	{
		try
		{
			Class<?> clazz = Class.forName( canonicalName );
			repository = (V11) clazz
				.getDeclaredConstructor( URI.class )
				.newInstance( uri );
		}
		catch ( InvocationTargetException e )
		{
			handleException( e.getTargetException() );
		}
		catch ( ReflectiveOperationException e )
		{
			handleException( e );
		}
	}

	private void handleException( Throwable e )
	{
		if ( isTolerant )
		{
			repository = EmptyControlledVocabularyRepository.instance();
			LOGGER.warn( "Tolerant proxy ignores {}", e.getMessage() );
		}
		else
		{
			throw new IllegalStateException( e );
		}
	}

	@Override
	public Set<String> findCodeValues()
	{
		if ( repository == null )
		{
			unproxy();
		}
		return repository.findCodeValues();
	}

	@Override
	public Set<String> findDescriptiveTerms()
	{
		if ( repository == null )
		{
			unproxy();
		}
		return repository.findDescriptiveTerms();
	}

	@Override
	public URI getUri()
	{
		return uri;
	}
}
