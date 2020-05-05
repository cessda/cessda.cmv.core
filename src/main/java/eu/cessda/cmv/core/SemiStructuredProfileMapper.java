package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;
import static org.gesis.commons.resource.Resource.newResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Function;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.gesis.commons.xml.DomDocument;
import org.gesis.commons.xml.XercesXalanDocument;
import org.w3c.dom.Node;

import eu.cessda.cmv.core.mediatype.profile.v0.xml.DdiProfileContraintsRoot;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbConstraintV0;

class SemiStructuredProfileMapper implements Function<URL, InputStream>
{
	public static InputStream map( URL url )
	{
		return new SemiStructuredProfileMapper().apply( url );
	}

	@Override
	public InputStream apply( URL url )
	{
		requireNonNull( url );

		DomDocument.V11 document = XercesXalanDocument.newBuilder()
				.ofInputStream( newResource( url ).readInputStream() )
				.printPrettyWithIndentation( 2 )
				.build();
		for ( Node usedNode : document.selectNodes( "/DDIProfile/Used" ) )
		{
			DdiProfileContraintsRoot root = new DdiProfileContraintsRoot();
			if ( document.selectNode( usedNode, "Description[Content='Required: Mandatory']" ) != null )
			{
				root.getConstraints().add( new JaxbConstraintV0( MandatoryNodeConstraint.class.getCanonicalName() ) );
			}
			if ( document.selectNode( usedNode, "Description[Content='Required: Recommended']" ) != null )
			{
				root.getConstraints().add( new JaxbConstraintV0( RecommendedNodeConstraint.class.getCanonicalName() ) );
			}
			if ( document.selectNode( usedNode, "Description[Content='Required: Optional']" ) != null )
			{
				root.getConstraints().add( new JaxbConstraintV0( "eu.cessda.cmv.core.OptionalNodeConstraint" ) );
			}
			if ( root.getConstraints().isEmpty() )
			{
				// String locationPath = usedNode.getAttributes().getNamedItem( "xpath"
				// ).getNodeValue();
				// System.out.println( String.format( "Any constraints for %s ?", locationPath ) );
			}
			else
			{
				Node contentNode = document.createElement( "r:Content" );
				contentNode.appendChild( document.createCDATASection( toXml( root ) ) );
				Node instructionNode = document.createElement( "pr:Instructions" );
				instructionNode.appendChild( contentNode );
				usedNode.appendChild( instructionNode );
			}
		}
		return toInputStream( document );
	}

	private InputStream toInputStream( DomDocument.V11 document )
	{
		try
		{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Source xmlSource = new DOMSource( document );
			Result outputTarget = new StreamResult( outputStream );
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
			transformer.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "2" );
			transformer.transform( xmlSource, outputTarget );
			return new ByteArrayInputStream( outputStream.toByteArray() );
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException( e );
		}
	}

	private String toXml( DdiProfileContraintsRoot root )
	{
		return XercesXalanDocument.newBuilder()
				.ofContent( root.toString() )
				.printPrettyWithIndentation( 0 )
				.build()
				.omitWhitespaceOnlyTextNodes()
				.getContent()
				.replace( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "" );
	}
}
