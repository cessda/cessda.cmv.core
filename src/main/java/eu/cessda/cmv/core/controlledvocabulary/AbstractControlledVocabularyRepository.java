package eu.cessda.cmv.core.controlledvocabulary;

import java.net.URI;
import java.util.Set;

abstract class AbstractControlledVocabularyRepository implements ControlledVocabularyRepository.V11
{
	private URI uri;
	private Set<String> codeValues;
	private Set<String> descriptiveTerms;

	protected void setUri( URI uri )
	{
		this.uri = uri;
	}

	protected void setCodeValues( Set<String> codeValues )
	{
		this.codeValues = codeValues;
	}

	protected void setDescriptiveTerms( Set<String> descriptiveTerms )
	{
		this.descriptiveTerms = descriptiveTerms;
	}

	@Override
	public Set<String> findCodeValues()
	{
		return codeValues;
	}

	@Override
	public Set<String> findDescriptiveTerms()
	{
		return descriptiveTerms;
	}

	@Override
	public URI getUri()
	{
		return uri;
	}
}
