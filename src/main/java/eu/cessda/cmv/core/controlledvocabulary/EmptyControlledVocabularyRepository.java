package eu.cessda.cmv.core.controlledvocabulary;

import static java.util.Collections.emptySet;

import java.util.Set;

public class EmptyControlledVocabularyRepository implements ControlledVocabularyRepository.V11
{
	@Override
	public Set<String> findCodeValues()
	{
		return emptySet();
	}

	@Override
	public Set<String> findDescriptiveTerms()
	{
		return emptySet();
	}
}
