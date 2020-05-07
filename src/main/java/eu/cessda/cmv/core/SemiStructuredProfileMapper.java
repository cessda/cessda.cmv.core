package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;
import static org.gesis.commons.resource.Resource.newResource;

import java.io.InputStream;
import java.net.URL;
import java.util.function.Function;

import org.gesis.commons.xml.DomDocument;
import org.gesis.commons.xml.XercesXalanDocument;
import org.w3c.dom.Node;

import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbDdiProfileConstraintsV0;

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

		DomDocument.V13 document = XercesXalanDocument.newBuilder()
				.ofInputStream( newResource( url ).readInputStream() )
				.printPrettyWithIndentation( 2 )
				.build();
		for ( Node usedNode : document.selectNodes( "/DDIProfile/Used" ) )
		{
			JaxbDdiProfileConstraintsV0 root = new JaxbDdiProfileConstraintsV0();
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
		return document.toInputStream();
	}

	private String toXml( JaxbDdiProfileConstraintsV0 root )
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
