package eu.cessda.cmv.core.controlledvocabulary;

import static org.gesis.commons.resource.Resource.newResource;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class CessdaControlledVocabularyRepository implements ControlledVocabularyRepository.V10
{
	private Set<String> codeValues;

	public CessdaControlledVocabularyRepository( URI uri )
	{
		Resource resource = new TextResource( newResource( uri ) );
		Object document = Configuration.defaultConfiguration().jsonProvider().parse( resource.toString() );
		List<String> list = JsonPath.read( document, "$.conceptAsMap.*.notation" );
		codeValues = list.stream().collect( Collectors.toSet() );
	}

	@Override
	public Set<String> findCodeValues()
	{
		return codeValues;
	}
}
