/*-
 * #%L
 * cmv-core
 * %%
 * Copyright (C) 2020 - 2024 CESSDA ERIC
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.*;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static javax.xml.xpath.XPathConstants.NODESET;

/**
 * Abstracts a {@link Document} and provides easy-to-use XPath APIs using a single object.
 */
public class XMLDocument
{

	private final Document document;
	private final XPathFactory xPathFactory = XPathFactory.newInstance();
	private final Map<String, XPathExpression> xPathExpressionMap = new HashMap<>();

	private NamespaceContext namespaceContext = new NamespaceContextImpl();

	private XMLDocument( Document document, int prettyPrintIndentation )
    {
		if ( prettyPrintIndentation < 0 )
		{
			throw new IllegalArgumentException( "Indentation for pretty printing must be non-negative" );
		}

        this.document = document;
	}

	public void setNamespaceContext( NamespaceContext namespaceContext)
	{
		this.namespaceContext = namespaceContext;
	}

	/**
	 * Set the root of the document to the element located at the given XPath.
	 * The document is modified by this method.
	 *
	 * @param location the XPath of the element that should become the root.
	 * @param nsContext the namespace context of the XPath.
	 * @throws XPathExpressionException if the XPath cannot be evaluated.
	 */
	public void setRootElement( String location, NamespaceContext nsContext ) throws XPathExpressionException
	{
		XPath xpath = xPathFactory.newXPath();
		xpath.setNamespaceContext( nsContext );
		XPathExpression metadataXPath = xpath.compile( location );

		NodeList metadataElements = (NodeList) metadataXPath.evaluate( document, XPathConstants.NODESET );
		Node metadataElement = metadataElements.item( 0 );

		if (metadataElement instanceof Element)
		{
			// Move the found element to the root of the document
			document.removeChild( document.getDocumentElement() );
			document.appendChild( metadataElement );
			document.getDocumentElement().setAttribute( "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance" );
		}
		else
		{
			throw new XPathExpressionException("No element matching " + location + " found");
		}
	}

	public Node selectNode( String xPath ) throws XPathExpressionException
	{
		return selectNode( document.getDocumentElement(), xPath );
	}

	public Node selectNode( Node context, String xPath ) throws XPathExpressionException
	{
		XPathExpression expression = newXPathExpression( xPath );
		return (Node) expression.evaluate( context, XPathConstants.NODE );
	}

	public List<Node> selectNodes( Node context, String locationPath ) throws XPathExpressionException
	{
		requireNonNull( context );
		requireNonNull( locationPath );

		XPathExpression expression = newXPathExpression( locationPath );
		NodeList nodeList = (NodeList) expression.evaluate( context, NODESET );
		ArrayList<Node> nodes = new ArrayList<>(nodeList.getLength());
		for ( int i = 0; i < nodeList.getLength(); i++ )
		{
			nodes.add( nodeList.item( i ) );
		}
		return nodes;
	}

	public List<Node> selectNodes( String xPath ) throws XPathExpressionException
	{
		return selectNodes( document.getDocumentElement(), xPath );
	}

	private XPathExpression newXPathExpression( String locationPath ) throws XPathExpressionException
	{
		XPathExpression xPathExpression = xPathExpressionMap.get( locationPath );

        if ( xPathExpression == null )
        {
			// XPathExpression is not cached, compile
            XPath xpath = xPathFactory.newXPath();
			xpath.setNamespaceContext( namespaceContext );
			xPathExpression = xpath.compile( locationPath  );

			// Cache the compiled XPathExpression for future use
            xPathExpressionMap.put( locationPath, xPathExpression );
        }

        return xPathExpression;
    }

	public Optional<LocationInfo> getLocationInfo( Node node )
	{
		requireNonNull( node );
		return ofNullable( (LocationInfo) node.getUserData( LocationInfo.class.getCanonicalName() ) );
	}

	/**
	 * The namespace of the root element of the document.
	 */
	public String getNamespace()
	{
		return document.getDocumentElement().getNamespaceURI();
	}

	public static Builder newBuilder()
	{
		return new Builder();
	}

	public static class Builder
	{
		private static final ThreadLocal<DocumentBuilder> documentBuilder = ThreadLocal.withInitial( () -> {
			try
			{
				DocumentBuilderFactory factory = getDocumentBuilderFactory();
				factory.setNamespaceAware( true );
				return factory.newDocumentBuilder();
			}
			catch ( ParserConfigurationException e )
			{
				throw new IllegalStateException(e);
			}
		} );

		private static final ThreadLocal<SAXParser> saxParser = ThreadLocal.withInitial( () -> {
			try
			{
				SAXParserFactory factory = getSaxParserFactory();
				factory.setNamespaceAware( true );
				return factory.newSAXParser();
			}
			catch ( ParserConfigurationException | SAXException e )
			{
				throw new IllegalStateException(e);
			}
		} );

		private static DocumentBuilderFactory getDocumentBuilderFactory() throws ParserConfigurationException
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			try
			{
				factory.setFeature( "http://apache.org/xml/features/disallow-doctype-decl", true );
				return factory;
			}
			catch ( ParserConfigurationException e )
			{
				// try the next one
			}

			try
			{
				factory.setFeature( "http://xml.org/sax/features/external-general-entities", false );
				factory.setFeature( "http://xml.org/sax/features/external-parameter-entities", false );
				return factory;
			}
			catch ( ParserConfigurationException e )
			{
				// try the next one
			}

			// All JAXP 1.5 parsers are required to support these attributes
			factory.setAttribute( XMLConstants.ACCESS_EXTERNAL_DTD, "" );
			factory.setAttribute( XMLConstants.ACCESS_EXTERNAL_SCHEMA, "" );
			return factory;
		}

		private static SAXParserFactory getSaxParserFactory() throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			try
			{
				factory.setFeature( "http://apache.org/xml/features/disallow-doctype-decl", true );
				return factory;
			}
			catch ( ParserConfigurationException e )
			{
				// try the next one
			}

			try
			{
				factory.setFeature( "http://xml.org/sax/features/external-general-entities", false );
				factory.setFeature( "http://xml.org/sax/features/external-parameter-entities", false );
				return factory;
			}
			catch ( ParserConfigurationException e )
			{
				// return without setting
				return factory;
			}
		}

		private boolean isLocationInfoAware = false;
		private int prettyPrintIndentation = 0;

		public Builder printPrettyWithIndentation( int indentation )
		{
			this.prettyPrintIndentation = indentation;
			return this;
		}

		public Builder locationInfoAware( boolean locationInfoAware )
		{
			this.isLocationInfoAware = locationInfoAware;
			return this;
		}

		public XMLDocument build( InputSource inputSource ) throws IOException, SAXException
		{
			Document document = parseInputSource( inputSource );
			return new XMLDocument( document, prettyPrintIndentation );
		}

		private Document parseLocationInfoAware( InputSource inputSource, Document document ) throws SAXException, IOException
		{
			SAXParser parser = saxParser.get();
			parser.parse( inputSource, new LocationInfoHandler( document ) );
			return document;
		}

		private Document parseInputSource( InputSource inputSource ) throws IOException, SAXException
		{
			DocumentBuilder builder = documentBuilder.get();

			if ( isLocationInfoAware )
			{
				return parseLocationInfoAware( inputSource, builder.newDocument() );
			}
			else
			{
				return builder.parse( inputSource );
			}
		}
	}
}
