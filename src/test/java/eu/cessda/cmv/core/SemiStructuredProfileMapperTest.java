package eu.cessda.cmv.core;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;
import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.gesis.commons.resource.TextResource;
import org.junit.jupiter.api.Test;

public class SemiStructuredProfileMapperTest
{
	@Test
	public void map() throws IOException
	{
		URL sourceUrl = getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" );
		File targetFile = new File( "target/cdc25_profile_converted.xml" );
		copyInputStreamToFile( SemiStructuredProfileMapper.map( sourceUrl ), targetFile );

		assertThat( targetFile, anExistingFile() );
		System.out.println( new TextResource( newResource( targetFile ) ).toString() );
	}
}
