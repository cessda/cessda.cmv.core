package eu.cessda.cmv.core;

import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.io.FileMatchers.anExistingFile;

import java.io.File;
import java.net.URL;

import org.junit.jupiter.api.Test;

import eu.cessda.cmv.core.mediatype.profile.v0.ProfileV0;

class DomSemiStructuredDdiProfileTest
{
	private CessdaMetadataValidatorFactory factory;

	DomSemiStructuredDdiProfileTest()
	{
		factory = new CessdaMetadataValidatorFactory();
	}

	@Test
	void construct()
	{
		// given
		URL url = getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" );

		// when
		Profile.V10 profile = factory.newProfile( url );

		// then
		assertThat( profile.getConstraints(), hasSize( 62 * 3 ) );
	}

	@Test
	void toJaxbProfileV0()
	{
		// given
		URL sourceUrl = getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" );
		File targetFile = new File( "src/main/resources/", "demo-documents/ddi-v25/cdc25_profile_cmv.xml" );

		// when
		DomSemiStructuredDdiProfile profile = new DomSemiStructuredDdiProfile(
				factory.newDdiInputStream( newResource( sourceUrl ).readInputStream() ) );
		ProfileV0 jaxbProfile = profile.toJaxbProfileV0();
		jaxbProfile.saveAs( targetFile );
		assertThat( targetFile, anExistingFile() );
		assertThat( jaxbProfile.getConstraints(), hasSize( 62 * 3 ) );
	}
}
