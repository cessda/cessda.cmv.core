/*-
 * #%L
 * cmv-core
 * %%
 * Copyright (C) 2020 - 2021 CESSDA ERIC
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package eu.cessda.cmv.core;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.StringToUrlMapper;
import org.gesis.commons.resource.TextResource;
import org.gesis.commons.xml.XercesXalanDocument;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.gesis.commons.resource.Resource.newResource;

class DemoDocumentsTest
{
	@Test
	@Disabled( "Code for dev work in progress" )
	@SuppressWarnings( "java:S2699" )
	void printPretty()
	{
		Stream.of( "src/test/resources/eu.cessda.cmv.core.MandatoryNodeConstraintTest" )
				.map( path -> asList( Objects.requireNonNull( new File( path ).listFiles() ) ) )
				.flatMap( List::stream )
				.filter( file -> file.getName().endsWith( ".xml" ) )
				.forEach( file -> XercesXalanDocument.newBuilder()
						.ofInputStream( newResource( file ).readInputStream() )
						.printPrettyWithIndentation( 2 )
						.build()
						.omitWhitespaceOnlyTextNodes()
						.saveAs( file ) );
	}

	@Test
	@Disabled( "Code for dev work in progress" )
	@SuppressWarnings( "java:S2699" )
	void listAllCvUrls()
	{
		Stream.of( "fsd-3271.xml", "ukds-2000.xml", "ukds-7481.xml", "gesis-2800.xml", "gesis-5100.xml", "gesis-5300.xml" )
				.map( fileName -> getClass().getResource( "/demo-documents/ddi-v25/" + fileName ) )
				.filter( Objects::nonNull )
				.map( Resource::newResource )
				.map( Resource.class::cast )
				.map( resource -> XercesXalanDocument.newBuilder().ofInputStream( resource.readInputStream() ).build() )
				.map( document -> document.selectNodes( "//@vocabURI" ) )
				.flatMap( List::stream )
				.map( Node::getTextContent )
				.distinct()
				.map( new StringToUrlMapper() )
				.map( Resource::newResource )
				.map( Resource.class::cast )
				.map( TextResource::new )
				.forEach( resource ->
				{
					System.out.println( resource.getUri() );
					System.out.println( resource );
				} );
	}
}
