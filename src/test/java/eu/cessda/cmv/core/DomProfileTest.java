package eu.cessda.cmv.core;

import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.junit.jupiter.api.Test;

public class DomProfileTest
{
	@Test
	public void test()
	{
		URL url = getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile_cmv.xml" );
		DomProfile profile = new DomProfile( newResource( url ).readInputStream() );
		assertThat( profile.getConstraints(), hasSize( 62 * 3 ) );
	}
}
