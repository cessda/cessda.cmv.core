package eu.cessda.cmv.core.controlledvocabulary;

import static java.util.Objects.requireNonNull;
import static org.gesis.commons.resource.Resource.newResource;
import static org.springframework.http.HttpMethod.GET;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class CessdaControlledVocabularyRepositoryV2 implements ControlledVocabularyRepository.V11
{
	private static final Logger LOGGER = LoggerFactory.getLogger( CessdaControlledVocabularyRepositoryV2.class );

	private URI uri;
	private Set<String> codeValues;
	private Set<String> descriptiveTerms;

	public CessdaControlledVocabularyRepositoryV2( URI uri )
	{
		this( (Resource) newResource( requireNonNull( uri ) ) );
	}

	public CessdaControlledVocabularyRepositoryV2( Resource resource )
	{
		requireNonNull( resource );

		uri = resource.getUri();
		Object document = null;
		if ( resource.getUri().getScheme().startsWith( "http" ) )
		{
			try
			{
				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setAccept( Collections.singletonList( MediaType.APPLICATION_JSON ) );
				HttpEntity<String> entity = new HttpEntity<>( "accept", headers );
				ResponseEntity<String> responseEntity = restTemplate.exchange( uri, GET, entity, String.class );
				document = Configuration.defaultConfiguration().jsonProvider().parse( responseEntity.getBody() );
			}
			catch (Exception e)
			{
				throw new IllegalArgumentException( "Resource not found" );
			}
		}
		else
		{
			document = Configuration.defaultConfiguration().jsonProvider()
					.parse( new TextResource( resource ).toString() );
		}
		List<String> list = JsonPath.read( document, "$.versions.*.concepts.*.notation" );
		codeValues = list.stream().collect( Collectors.toSet() );
		list = JsonPath.read( document, "$.versions.*.concepts.*.title" );
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

	@Override
	public URI getUri()
	{
		return uri;
	}
}
