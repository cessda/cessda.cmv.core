package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.Factory.newProfile;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URL;

import org.junit.jupiter.api.Test;

public class DomProfileTest
{
	@Test
	public void construct() throws IOException
	{
		// given
		URL url = getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" );

		// when
		Profile.V10 profile = newProfile( url );

		// then
		assertThat( profile.getConstraints(), hasSize( 62 ) );
	}
}
