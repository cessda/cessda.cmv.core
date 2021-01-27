package eu.cessda.cmv.core.controlledvocabulary;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControlledVocabularyRepositoryProxy implements ControlledVocabularyRepository.V11
{
	private static final Logger LOGGER = LoggerFactory.getLogger( ControlledVocabularyRepositoryProxy.class );

	private String uri;
	private String canonicalName;
	private ControlledVocabularyRepository.V11 repository;
	private boolean isTolerant;

	public ControlledVocabularyRepositoryProxy( String canonicalName, String uri )
	{
		this( canonicalName, uri, false );
	}

	public ControlledVocabularyRepositoryProxy( String canonicalName, String uri, boolean isTolerant )
	{
		requireNonNull( canonicalName );
		requireNonNull( uri );

		this.canonicalName = canonicalName;
		this.uri = uri;
		this.isTolerant = isTolerant;
	}

	public void unproxy()
	{
		try
		{
			Class<?> clazz = Class.forName( canonicalName );
			repository = (eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepository.V11) clazz
					.getDeclaredConstructor( URI.class )
					.newInstance( new URI( uri ) );
		}
		catch (InvocationTargetException e)
		{
			handleException( e.getTargetException().getMessage(), e.getTargetException() );
		}
		catch (Exception e)
		{
			handleException( e.getMessage(), e );
		}
	}

	private void handleException( String message, Throwable e )
	{
		if ( isTolerant )
		{
			repository = new EmptyControlledVocabularyRepository();
			LOGGER.warn( "Tolerant proxy ignores {}", e.getMessage() );
		}
		else
		{
			throw new IllegalArgumentException( message );
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
		return repository.getUri();
	}
}
