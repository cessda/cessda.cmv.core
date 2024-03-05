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
import org.gesis.commons.xml.XMLDocument;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

class DemoDocumentsTest
{
	@Test
	@Disabled( "Code for dev work in progress" )
	@SuppressWarnings( "java:S2699" )
	void listAllCvUrls()
	{
		Stream.of( "fsd-3271.xml", "ukds-2000.xml", "ukds-7481.xml", "gesis-2800.xml", "gesis-5100.xml", "gesis-5300.xml" )
				.map( fileName -> getClass().getResource( "/demo-documents/ddi-v25/" + fileName ) )
				.filter( Objects::nonNull )
				.map( resource ->
                {
                    try
                    {
                        return XMLDocument.newBuilder().source( resource ).build();
                    }
                    catch ( IOException | SAXException e )
                    {
						throw new AssertionFailedError(null, e);
                    }
                } )
				.map( document ->
                {
                    try
                    {
                        return document.selectNodes( "//@vocabURI" );
                    }
                    catch ( XPathExpressionException e )
                    {
						throw new AssertionError(e);
                    }
                } )
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
