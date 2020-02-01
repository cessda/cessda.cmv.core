package cessda.cmv.core;

import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;
import org.gesis.commons.xml.DomDocument;
import org.gesis.commons.xml.XercesXalanDocument;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

public class ValidationTest
{
	@Test
	public void inspectDocuments() throws IOException
	{
		DomDocument.V10 metadataDocument = newDocument( new File( "src/test/resources/ddi-v25/ukds-7481.xml" ) );
		assertThat( metadataDocument.getElementXPaths(), hasSize( 73 ) );

		DomDocument.V10 profileDocument = newDocument( new File( "src/test/resources/ddi-v25/cdc_profile.xml" ) );
		assertThat( profileDocument.getElementXPaths(), hasSize( 17 ) );
		List<Node> nodes = profileDocument.selectNodes( "/DDIProfile/Used/@xpath" );
		assertThat( nodes, hasSize( 88 ) );
		nodes = profileDocument.selectNodes( "/DDIProfile/Used[@isRequired='true']/@xpath" );
		assertThat( nodes, hasSize( 42 ) );
		nodes = profileDocument.selectNodes( "/DDIProfile/Used[@isRequired='false']/@xpath" );
		assertThat( nodes, hasSize( 88 - 42 ) );
	}

	private DomDocument.V10 newDocument( File file )
	{
		return newDocument( file.toURI() );
	}

	private DomDocument.V10 newDocument( URI uri )
	{
		Resource resource = new TextResource( newResource( uri ) );
		return XercesXalanDocument.newBuilder()
				.ofContent( resource.toString() )
				.namespaceUnaware()
				.printPrettyWithIndentation( 2 )
				.build();
	}
}