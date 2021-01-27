package eu.cessda.cmv.core.controlledvocabulary;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControlledVocabularyRepositoryProxy implements ControlledVocabularyRepository.V11
{
	private static final Logger LOGGER = LoggerFactory.getLogger( ControlledVocabularyRepositoryProxy.class );

	private String canonicalName;
	private String uri;
	private ControlledVocabularyRepository.V11 repository;

	public ControlledVocabularyRepositoryProxy( String canonicalName, String uri )
	{
		requireNonNull( canonicalName );
		requireNonNull( uri );

		this.canonicalName = canonicalName;
		this.uri = uri;
	}

	public void unproxy()
	{
		Class<?> clazz;
		try
		{
			clazz = Class.forName( canonicalName );
		}
		catch (ClassNotFoundException e)
		{
			throw new IllegalArgumentException( e.getMessage() );
		}
		try
		{
			repository = (eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepository.V11) clazz
					.getDeclaredConstructor( URI.class )
					.newInstance( new URI( uri ) );
		}
		catch (Exception e)
		{
			repository = new EmptyControlledVocabularyRepository();
			LOGGER.error( e.getMessage(), e );
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
