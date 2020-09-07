package eu.cessda.cmv.core.controlledvocabulary;

import static java.util.Objects.requireNonNull;
import static org.gesis.commons.resource.Resource.newResource;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class CessdaControlledVocabularyRepository implements ControlledVocabularyRepository.V11
{
	private Set<String> codeValues;
	private Set<String> descriptiveTerms;

	public CessdaControlledVocabularyRepository( URI uri )
	{
		this( newResource( requireNonNull( uri ) ) );
	}

	public CessdaControlledVocabularyRepository( Resource resource )
	{
		requireNonNull( resource );

		Object document = Configuration.defaultConfiguration().jsonProvider()
				.parse( new TextResource( resource ).toString() );
		List<String> list = JsonPath.read( document, "$.conceptAsMap.*.notation" );
		codeValues = list.stream().collect( Collectors.toSet() );
		list = JsonPath.read( document, "$.conceptAsMap.*.title" );
		descriptiveTerms = list.stream().collect( Collectors.toSet() );
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
}
