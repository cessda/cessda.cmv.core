package eu.cessda.cmv.core;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.io.FileMatchers.anExistingFile;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;
import org.gesis.commons.xml.XercesXalanDocument;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

public class DemoDocumentsTest
{
	private List<String> demoDocumentFileNames = asList( "fsd-3271.xml", "ukds-2000.xml", "ukds-7481.xml" );

	@Test
	public void printPretty()
	{
		demoDocumentFileNames.forEach( fileName ->
		{
			URL sourceUrl = getClass().getResource( "/demo-documents/ddi-v25/" + fileName );
			File targetFile = new File( "src/main/resources/demo-documents/ddi-v25", fileName );
			assertThat( targetFile, anExistingFile() );

			XercesXalanDocument.newBuilder()
					.ofInputStream( newResource( sourceUrl ).readInputStream() )
					.printPrettyWithIndentation( 2 )
					.build()
					.omitWhitespaceOnlyTextNodes()
					.saveAs( targetFile );
		} );
	}

	@Test
	public void listAllCvUrls()
	{
		demoDocumentFileNames.stream()
				.map( fileName -> getClass().getResource( "/demo-documents/ddi-v25/" + fileName ) )
				.map( url -> newResource( url ) )
				.map( resource -> XercesXalanDocument.newBuilder().ofInputStream( resource.readInputStream() ).build() )
				.map( document -> document.selectNodes( "//@vocabURI" ) )
				.flatMap( List::stream )
				.map( Node::getTextContent )
				.distinct()
				.map( this::newUrl )
				.map( Resource::newResource )
				.map( TextResource::new )
				.forEach( resource ->
				{
					System.out.println( resource.getUri() );
					System.out.println( resource.toString() );
				} );
	}

	private URL newUrl( String url )
	{
		requireNonNull( url );
		try
		{
			return new URL( url );
		}
		catch (MalformedURLException e)
		{
			throw new IllegalArgumentException( e );
		}
	}
}
