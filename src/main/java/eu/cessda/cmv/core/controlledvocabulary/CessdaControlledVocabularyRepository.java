package eu.cessda.cmv.core.controlledvocabulary;

import static java.util.Objects.requireNonNull;
import static org.gesis.commons.resource.Resource.newResource;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class CessdaControlledVocabularyRepository extends AbstractControlledVocabularyRepository
{
	public CessdaControlledVocabularyRepository( URI uri )
	{
		this( (Resource) newResource( requireNonNull( uri ) ) );
	}

	public CessdaControlledVocabularyRepository( Resource resource )
	{
		requireNonNull( resource );
		setUri( resource.getUri() );
		Object document = Configuration.defaultConfiguration().jsonProvider()
				.parse( new TextResource( resource ).toString() );
		List<String> list = JsonPath.read( document, "$.conceptAsMap.*.notation" );
		setCodeValues( list.stream().collect( Collectors.toSet() ) );
		list = JsonPath.read( document, "$.conceptAsMap.*.title" );
		setDescriptiveTerms( list.stream().collect( Collectors.toSet() ) );
	}
}
