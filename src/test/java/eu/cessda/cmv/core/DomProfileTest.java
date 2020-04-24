package eu.cessda.cmv.core;

import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URL;

import org.gesis.commons.xml.ddi.DdiInputStream;
import org.junit.jupiter.api.Test;

public class DomProfileTest
{
	@Test
	public void construct() throws IOException
	{
		// given
		URL profileFile = getClass().getResource( "/ddi-v25/cdc25_profile.xml" );
		try ( DdiInputStream profileInputStream = new DdiInputStream( newResource( profileFile ).readInputStream() ) )
		{
			// when
			Profile.V10 profile = new DomProfile( profileInputStream );

			// then
			assertThat( profile.getConstraints(), hasSize( 64 ) );
		}
	}
}
