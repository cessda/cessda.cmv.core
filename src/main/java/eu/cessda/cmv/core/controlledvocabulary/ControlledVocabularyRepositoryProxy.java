package eu.cessda.cmv.core.controlledvocabulary;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Set;

public class ControlledVocabularyRepositoryProxy implements ControlledVocabularyRepository.V10
{
	private String canonicalName;
	private String uri;
	private ControlledVocabularyRepository.V10 repository;

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
			repository = (eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepository.V10) clazz
					.getDeclaredConstructor( URI.class )
					.newInstance( new URI( uri ) );
		}
		catch (InvocationTargetException e)
		{
			throw new IllegalArgumentException( e.getTargetException() );
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException( e.getMessage() );
		}
	}

	@Override
	public Set<String> findCodeValues()
	{
		return repository.findCodeValues();
	}
}