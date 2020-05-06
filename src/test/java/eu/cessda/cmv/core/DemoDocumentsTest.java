package eu.cessda.cmv.core;

import static org.gesis.commons.resource.Resource.newResource;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

import org.gesis.commons.xml.XercesXalanDocument;
import org.junit.jupiter.api.Test;

public class DemoDocumentsTest
{
	@Test
	public void printPretty()
	{
		Arrays.asList( "fsd-3271.xml", "ukds-2000.xml", "ukds-7481.xml" ).forEach( fileName ->
		{
			URL url = getClass().getResource( "/demo-documents/ddi-v25/" + fileName );
			XercesXalanDocument.newBuilder()
					.ofInputStream( newResource( url ).readInputStream() )
					.printPrettyWithIndentation( 2 )
					.build()
					.omitWhitespaceOnlyTextNodes()
					.saveAs( new File( "src/main/resources/demo-documents/ddi-v25", fileName ) );
		} );
	}
}
