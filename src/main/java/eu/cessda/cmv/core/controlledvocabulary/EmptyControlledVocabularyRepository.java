package eu.cessda.cmv.core.controlledvocabulary;

import static java.util.Collections.emptySet;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

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

	@Override
	public URI getUri()
	{
		return URI.create( "urn:uuid:" + UUID.randomUUID().toString() );
	}
}
