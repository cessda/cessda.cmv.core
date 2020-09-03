package eu.cessda.cmv.core;

import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.net.URL;

import org.junit.jupiter.api.Test;

class DomProfileTest
{
	@Test
	void test()
	{
		URL url = getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile_cmv.xml" );
		DomProfile profile = new DomProfile( newResource( url ).readInputStream() );
		assertThat( profile.getConstraints(), hasSize( 62 * 3 ) );
	}
}
