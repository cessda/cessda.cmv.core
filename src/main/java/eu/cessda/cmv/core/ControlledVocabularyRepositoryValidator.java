package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;

import java.util.Optional;

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepository;
import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepositoryProxy;

class ControlledVocabularyRepositoryValidator implements Validator.V10
{
	private ControlledVocabularyRepositoryProxy proxy;

	ControlledVocabularyRepositoryValidator( ControlledVocabularyRepositoryProxy proxy )
	{
		requireNonNull( proxy );
		this.proxy = proxy;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		try
		{
			proxy.unproxy();
		}
		catch (Exception e)
		{
			return Optional.of( new ConstraintViolation( e.getMessage(), empty() ) );
		}
		if ( ((ControlledVocabularyRepository.V10) proxy).findCodeValues().isEmpty() )
		{
			return Optional.of( new ConstraintViolation( "No values", empty() ) );
		}
		return empty();
	}
}
