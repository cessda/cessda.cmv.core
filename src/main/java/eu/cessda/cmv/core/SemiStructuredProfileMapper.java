package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;
import static org.gesis.commons.resource.Resource.newResource;

import java.io.InputStream;
import java.net.URL;
import java.util.function.Function;

import org.gesis.commons.xml.DomDocument;
import org.gesis.commons.xml.XercesXalanDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbDdiProfileConstraintsV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbMandatoryNodeConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbOptionalNodeConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbRecommendedNodeConstraintV0;

class SemiStructuredProfileMapper implements Function<URL, InputStream>
{
	private static final Logger LOGGER = LoggerFactory.getLogger( SemiStructuredProfileMapper.class );

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
			JaxbConstraintV0 constraint;
			JaxbDdiProfileConstraintsV0 root = new JaxbDdiProfileConstraintsV0();
			if ( document.selectNode( usedNode, "Description[Content='Required: Mandatory']" ) != null )
			{
				root.getConstraints().add( new JaxbMandatoryNodeConstraintV0( getLocationPath( usedNode ) ) );
			}
			if ( document.selectNode( usedNode, "Description[Content='Required: Recommended']" ) != null )
			{
				root.getConstraints().add( new JaxbRecommendedNodeConstraintV0( getLocationPath( usedNode ) ) );
			}
			if ( document.selectNode( usedNode, "Description[Content='Required: Optional']" ) != null )
			{
				root.getConstraints().add( new JaxbOptionalNodeConstraintV0( getLocationPath( usedNode ) ) );
			}
			if ( root.getConstraints().isEmpty() )
			{
				String locationPath = usedNode.getAttributes().getNamedItem( "xpath" ).getNodeValue();
				LOGGER.warn( "Any constraints for {}?", locationPath );
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

	private String getLocationPath( org.w3c.dom.Node usedNode )
	{
		return usedNode.getAttributes().getNamedItem( "xpath" ).getTextContent();
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
