package eu.cessda.cmv.core.controlledvocabulary;

import java.net.URI;
import java.util.Set;

public interface ControlledVocabularyRepository
{
	public interface V10 extends ControlledVocabularyRepository
	{
		public Set<String> findCodeValues();
	}

	public interface V11 extends V10
	{
		public Set<String> findDescriptiveTerms();

		public URI getUri();
	}
}
