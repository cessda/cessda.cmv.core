package eu.cessda.cmv.core.controlledvocabulary;

import static java.util.Objects.requireNonNull;
import static org.gesis.commons.resource.Resource.newResource;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class CessdaControlledVocabularyRepository implements ControlledVocabularyRepository.V11
{
	private static final Logger LOGGER = LoggerFactory.getLogger( CessdaControlledVocabularyRepository.class );

	private URI uri;
	private Set<String> codeValues;
	private Set<String> descriptiveTerms;

	public CessdaControlledVocabularyRepository( URI uri )
	{
		this( (Resource) newResource( requireNonNull( uri ) ) );
	}

	public CessdaControlledVocabularyRepository( Resource resource )
	{
		requireNonNull( resource );

		uri = resource.getUri();
		Object document = Configuration.defaultConfiguration().jsonProvider()
				.parse( new TextResource( resource ).toString() );
		List<String> list = query( document, "$.conceptAsMap.*.notation" );
		codeValues = list.stream().collect( Collectors.toSet() );
		list = query( document, "$.conceptAsMap.*.title" );
		descriptiveTerms = list.stream().collect( Collectors.toSet() );
	}

	private List<String> query( Object document, String query )
	{
		try
		{
			return JsonPath.read( document, query );
		}
		catch (Exception e)
		{
			LOGGER.error( e.getMessage(), e );
			return Collections.emptyList();
		}
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
