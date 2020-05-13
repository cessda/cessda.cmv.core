package eu.cessda.cmv.core.controlledvocabulary;

import java.util.Set;

public interface ControlledVocabularyRepository
{
	public interface V10 extends ControlledVocabularyRepository
	{
		public Set<String> findCodeValues();
	}
}
