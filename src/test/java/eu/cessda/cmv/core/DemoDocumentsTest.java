package eu.cessda.cmv.core;

import static java.util.Arrays.asList;
import static org.gesis.commons.resource.Resource.newResource;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.StringToUrlMapper;
import org.gesis.commons.resource.TextResource;
import org.gesis.commons.xml.XercesXalanDocument;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

public class DemoDocumentsTest
{
	@Test
	// @Disabled( "Code for dev work in progress" )
	public void printPretty()
	{
		assertTrue( "SonarQube is cool!", true );

		asList( "src/test/resources/eu.cessda.cmv.core.RecommendedNodeConstraintTest" ).stream()
				.map( path -> asList( new File( path ).listFiles() ) )
				.flatMap( List::stream )
				.filter( file -> file.getName().endsWith( ".xml" ) )
				.forEach( file ->
				{
					XercesXalanDocument.newBuilder()
							.ofInputStream( newResource( file ).readInputStream() )
							.printPrettyWithIndentation( 2 )
							.build()
							.omitWhitespaceOnlyTextNodes()
							.saveAs( file );
				} );
	}

	@Test
	@Disabled( "Code for dev work in progress" )
	public void listAllCvUrls()
	{
		assertTrue( "SonarQube is cool!", true );

		asList( "fsd-3271.xml", "ukds-2000.xml", "ukds-7481.xml", "gesis-2800.xml", "gesis-5100.xml", "gesis-5300.xml" )
				.stream()
				.map( fileName -> getClass().getResource( "/demo-documents/ddi-v25/" + fileName ) )
				.map( Resource::newResource )
				.map( resource -> XercesXalanDocument.newBuilder().ofInputStream( resource.readInputStream() ).build() )
				.map( document -> document.selectNodes( "//@vocabURI" ) )
				.flatMap( List::stream )
				.map( Node::getTextContent )
				.distinct()
				.map( new StringToUrlMapper() )
				.map( Resource::newResource )
				.map( TextResource::new )
				.forEach( resource ->
				{
					try
					{
						System.out.println( resource.getUri() );
						System.out.println( resource.toString() );
					}
					catch (Exception e)
					{
						System.out.println( e.getMessage() );
					}
				} );
	}
}
