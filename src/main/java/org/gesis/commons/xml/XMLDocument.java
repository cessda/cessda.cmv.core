package org.gesis.commons.xml;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.util.*;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static javax.xml.XMLConstants.XMLNS_ATTRIBUTE;
import static javax.xml.xpath.XPathConstants.NODESET;

public class XMLDocument
{

	private final Document document;
	private final SimpleNamespaceContext namespaceContext;
    private final boolean isNamespaceAware;
    private final XPathFactory xPathFactory = XPathFactory.newInstance();
	private final Map<String, XPathExpression> xPathExpressionMap = new HashMap<>();

	private XMLDocument( Document document, int prettyPrintIndentation, boolean isNamespaceAware )
    {
		if ( prettyPrintIndentation < 0 )
		{
			throw new IllegalArgumentException( "Indentation for pretty printing must be non-negative" );
		}

        this.document = document;
		this.isNamespaceAware = isNamespaceAware;
		this.namespaceContext = getSimpleNamespaceContext(document);
	}

	private static SimpleNamespaceContext getSimpleNamespaceContext(Document document)
	{
		SimpleNamespaceContext simpleNamespaceContext = new SimpleNamespaceContext( false );
		Element documentElement = document.getDocumentElement();
		if ( documentElement != null )
		{
			NamedNodeMap documentElementAttributes = documentElement.getAttributes();
			for ( int i = 0; i < documentElementAttributes.getLength(); i++ )
			{
				Node attribute = documentElementAttributes.item( i );
				if ( attribute.getNodeName().startsWith( XMLNS_ATTRIBUTE ) )
				{
					simpleNamespaceContext.bindNamespaceUri(
						attribute.getNodeName().replaceAll( "xmlns:?", "" ),
						attribute.getNodeValue()
					);
				}
			}
		}
		return simpleNamespaceContext;
	}

	public Node selectNode( String xPath ) throws XPathExpressionException
	{
		return selectNode( document.getDocumentElement(), xPath );
	}

	public Node selectNode( Node context, String xPath ) throws XPathExpressionException
	{
		return (Node) newXPathExpression( xPath ).evaluate( context, XPathConstants.NODE );
	}

	public List<Node> selectNodes( Node context, String locationPath ) throws XPathExpressionException
	{
		requireNonNull( context );
		requireNonNull( locationPath );

		NodeList nodeList = (NodeList) newXPathExpression( locationPath ).evaluate( context, NODESET );
		ArrayList<Node> nodes = new ArrayList<>();
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
		if ( xPathExpressionMap.containsKey( locationPath ) )
		{
			return xPathExpressionMap.get( locationPath );
		}
		else
		{
			XPath xpath = xPathFactory.newXPath();
			XPathExpression xPathExpression;
			if ( isNamespaceAware )
			{
				xpath.setNamespaceContext( namespaceContext );
                xPathExpression = xpath.compile( namespaceContext.decorateDefaultNamespace( locationPath ) );
			}
			else
			{
				xPathExpression = xpath.compile( locationPath );
			}
			xPathExpressionMap.put( locationPath, xPathExpression );
			return xPathExpression;
		}
	}

	public Optional<LocationInfo> getLocationInfo( Node node )
	{
		requireNonNull( node );
		return ofNullable( (LocationInfo) node.getUserData( LocationInfo.class.getCanonicalName() ) );
	}

	public static Builder newBuilder()
	{
		return new Builder();
	}

	public static class Builder
	{
		private static final ThreadLocal<DocumentBuilder> namespaceAwareDocumentBuilder = ThreadLocal.withInitial( () -> {
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

		private static final ThreadLocal<DocumentBuilder> namespaceUnawareDocumentBuilder = ThreadLocal.withInitial( () -> {
			try
			{
				DocumentBuilderFactory factory = getDocumentBuilderFactory();
				factory.setNamespaceAware( false );
				return factory.newDocumentBuilder();
			}
			catch ( ParserConfigurationException e )
			{
				throw new IllegalStateException(e);
			}
		} );



		private static final ThreadLocal<SAXParser> namespaceAwareSAXParser = ThreadLocal.withInitial( () -> {
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

		private static final ThreadLocal<SAXParser> namespaceUnawareSAXParser = ThreadLocal.withInitial( () -> {
			try
			{
				SAXParserFactory factory = getSaxParserFactory();
				factory.setNamespaceAware( false );
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
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			return factory;
		}

		private static SAXParserFactory getSaxParserFactory() throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			return factory;
		}

		private boolean isLocationInfoAware = false;
		private boolean isNamespaceAware = false;
		private int prettyPrintIndentation = 0;
		private InputSource inputSource;
		private Document document;

		public Builder()
		{
			printPrettyWithIndentation( 0 );
		}

		public Builder source( File file )
		{
			return source( file.toURI() );
		}

		public Builder source( URL url )
		{
			inputSource = new InputSource( url.toExternalForm() );
			return this;
		}

		public Builder source( URI uri )
		{
			inputSource = new InputSource( uri.toASCIIString() );
			return this;
		}

		public Builder source( String content )
		{
			inputSource = new InputSource( new StringReader( content ) );
			return this;
		}

		public Builder source( InputStream inputStream )
		{
			inputSource = new InputSource( inputStream );
			return this;
		}

		public Builder source( Document document )
		{
			this.document = document;
			return this;
		}

		public Builder printPrettyWithIndentation( Integer indentation )
		{
			this.prettyPrintIndentation = indentation;
			return this;
		}

		public Builder namespaceAware( boolean namespaceAware )
		{
			this.isNamespaceAware = namespaceAware;
			return this;
		}

		public Builder namespaceAware()
		{
			return namespaceAware( true );
		}

		public Builder namespaceUnaware()
		{
			return namespaceAware( false );
		}

		public Builder locationInfoAware( boolean locationInfoAware )
		{
			this.isLocationInfoAware = locationInfoAware;
			return this;
		}

		public XMLDocument build() throws IOException, SAXException
		{
			if (document == null)
			{
				document = parseInputSource( inputSource );
			}
			return new XMLDocument( document, prettyPrintIndentation, isNamespaceAware );
		}

		private Document parseLocationInfoAware( InputSource inputSource, Document document ) throws SAXException, IOException
		{
			SAXParser parser;
			if (isNamespaceAware)
			{
				parser = namespaceAwareSAXParser.get();
			}
			else
			{
				parser = namespaceUnawareSAXParser.get();
			}

			parser.parse( inputSource, new LocationInfoHandler( document ) );
			return document;
		}

		private Document parseInputSource( InputSource inputSource ) throws IOException, SAXException
		{

			DocumentBuilder builder;
			if (isNamespaceAware)
			{
				builder = namespaceAwareDocumentBuilder.get();
			}
			else
			{
				builder = namespaceUnawareDocumentBuilder.get();
			}

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
