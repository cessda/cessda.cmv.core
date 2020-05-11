package eu.cessda.cmv.core.mediatype.profile.v0.xml;

import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URL;

import org.junit.jupiter.api.Test;

public class JaxbProfileV0Test
{
	@Test
	public void readSemiStructuredDdiProfile()
	{
		URL sourceUrl = this.getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" );
		File targetFile = new File( "src/main/resources/", "demo-documents/ddi-v25/cdc25_profile_cmv.xml" );

		JaxbProfileV0 profile = JaxbProfileV0.readSemiStructuredDdiProfile( newResource( sourceUrl ) );
		profile.saveAs( targetFile );

		assertThat( targetFile, anExistingFile() );
		assertThat( profile.getConstraints(), hasSize( 46 ) );
	}
}
