/*-
 * #%L
 * cmv-core
 * %%
 * Copyright (C) 2020 - 2025 CESSDA ERIC
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
package org.gesis.commons.xml;

import eu.cessda.cmv.core.NamespaceContextImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayDeque;
import java.util.Deque;

import static javax.xml.XMLConstants.XMLNS_ATTRIBUTE;

/**
 * Adds {@link Locator} information as {@link LocationInfo} instances to the parsed XML document.
 */
public class LocationInfoAwareHandler extends DefaultHandler
{
	private final Document document;
	private final NamespaceContextImpl namespaceContext = new NamespaceContextImpl();
	private final Deque<Element> elementStack = new ArrayDeque<>();
	private final StringBuilder textBuffer = new StringBuilder();

	private Locator locator;

	public LocationInfoAwareHandler( Document document )
	{
		this.document = document;
	}

	@Override
	public void setDocumentLocator( Locator locator )
	{
		this.locator = locator;
	}

	@Override
	public void startPrefixMapping( String namespacePrefix, String namespaceUri )
	{
		namespaceContext.bindNamespaceURI( namespacePrefix, namespaceUri );
	}

	@Override
	public void startElement( String uri, String localName, String qName, Attributes attributes )
	{
		addTextIfNeeded();

		// Get location of element
		LocationInfo locationInfo = new LocationInfo( locator.getLineNumber(), locator.getColumnNumber() );

		// Create element
		Element element = document.createElementNS( uri, qName );
		element.setUserData( LocationInfo.class.getCanonicalName(), locationInfo, null );

		// Create attributes
		for ( int i = 0; i < attributes.getLength(); i++ )
		{
			Attr attr = document.createAttributeNS( attributes.getURI( i ), attributes.getQName( i ) );
			attr.setNodeValue( attributes.getValue( i ) );
			attr.setUserData( LocationInfo.class.getCanonicalName(), locationInfo, null );
			element.setAttributeNode( attr );
		}

		// Add element to the stack
		elementStack.push( element );
	}

	@Override
	public void endElement( String uri, String localName, String qName )
	{
		addTextIfNeeded();
		Element element = elementStack.pop();
		if ( elementStack.isEmpty() )
		{
			// https://git.gesis.org/java-commons/commons-xml/issues/68
			// append xmlns attributes
			namespaceContext.getAllBindings().forEach( (prefix, ns) ->
			{
				if (prefix.isEmpty())
				{
					// default namespace
					element.setAttribute( XMLNS_ATTRIBUTE, ns );
				}
				else
				{
					// prefixed namespace
					String name = XMLNS_ATTRIBUTE + ":" + prefix;
					element.setAttribute( name, ns );
				}
			} );

			// append root element to the document
			document.appendChild( element ); // is document element
		}
		else
		{
			// append element to parent
			Element parentElement = elementStack.peek();
			parentElement.appendChild( element );
		}
	}

	@Override
	public void characters( char[] ch, int start, int length )
	{
		textBuffer.append( ch, start, length );
	}

	private void addTextIfNeeded()
	{
		if ( textBuffer.length() > 0 )
		{
			Node textNode = document.createTextNode( textBuffer.toString() );

			// Peek at the current element
			Element element = elementStack.peek();
			if (element != null)
			{
				element.appendChild( textNode );
			}
			else
			{
				// No elements, append text directly to the document
				document.appendChild( textNode );
			}

			textBuffer.delete( 0, textBuffer.length() );
		}
	}
}
