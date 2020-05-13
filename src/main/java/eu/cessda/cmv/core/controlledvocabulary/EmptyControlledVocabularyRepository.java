package eu.cessda.cmv.core.controlledvocabulary;

import static java.util.Collections.emptySet;

import java.util.Set;

public class EmptyControlledVocabularyRepository implements ControlledVocabularyRepository.V10
{
	@Override
	public Set<String> findCodeValues()
	{
		return emptySet();
	}
}
