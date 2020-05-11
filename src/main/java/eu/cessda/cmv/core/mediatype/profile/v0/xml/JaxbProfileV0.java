package eu.cessda.cmv.core.mediatype.profile.v0.xml;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.xml.DomDocument;
import org.gesis.commons.xml.XercesXalanDocument;
import org.gesis.commons.xml.jaxb.DefaultNamespacePrefixMapper;
import org.gesis.commons.xml.jaxb.JaxbDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

@XmlRootElement( name = JaxbProfileV0.JAXB_ELEMENT )
@XmlType( name = JaxbProfileV0.JAXB_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class JaxbProfileV0 extends JaxbDocument
{
	private static final Logger LOGGER = LoggerFactory.getLogger( JaxbProfileV0.class );

	static final String MAJOR = "0";
	static final String MINOR = "1";
	static final String VERSION = MAJOR + "." + MINOR;
	public static final String MEDIATYPE = "application/vnd.eu.cessda.cmv.core.mediatype.profile.v" + VERSION + "+xml";
	static final String SCHEMALOCATION_HOST = "https://bitbucket.org/cessda/cessda.cmv.core/src/raw/stable/schema";
	public static final String SCHEMALOCATION_FILENAME = "profile-v" + VERSION + ".xsd";

	static final String NAMESPACE_DEFAULT_PREFIX = "";
	static final String NAMESPACE_DEFAULT_URI = "cmv:profile:v" + MAJOR;
	static final String SCHEMALOCATION_URI = SCHEMALOCATION_HOST + "/" + SCHEMALOCATION_FILENAME;
	static final String SCHEMALOCATION = NAMESPACE_DEFAULT_URI + " " + SCHEMALOCATION_URI;

	static final String JAXB_ELEMENT = "Profile";
	static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	private static final JAXBContext JAXBCONTEXT = newJaxbContext( JaxbProfileV0.class );

	@XmlElement
	private String name;

	@XmlElements( {
			@XmlElement(
					name = JaxbMaximumElementOccuranceConstraintV0.JAXB_ELEMENT,
					type = JaxbMaximumElementOccuranceConstraintV0.class ),
			@XmlElement(
					name = JaxbMandatoryNodeConstraintV0.JAXB_ELEMENT,
					type = JaxbMandatoryNodeConstraintV0.class ),
			@XmlElement(
					name = JaxbOptionalNodeConstraintV0.JAXB_ELEMENT,
					type = JaxbOptionalNodeConstraintV0.class ),
			@XmlElement(
					name = JaxbRecommendedNodeConstraintV0.JAXB_ELEMENT,
					type = JaxbRecommendedNodeConstraintV0.class )
	} )
	private List<JaxbConstraintV0> constraints;

	public JaxbProfileV0()
	{
		super( JaxbProfileV0.SCHEMALOCATION, new DefaultNamespacePrefixMapper( NAMESPACE_DEFAULT_URI ) );
		constraints = new ArrayList<>();
	}

	public String getName()
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public List<JaxbConstraintV0> getConstraints()
	{
		return constraints;
	}

	public void setConstraints( List<JaxbConstraintV0> constraints )
	{
		this.constraints = constraints;
	}

	public static JaxbProfileV0 read( InputStream inputStream )
	{
		return JaxbDocument.read( inputStream, JAXBCONTEXT );
	}

	public static JaxbProfileV0 open( File file ) throws NoSuchFileException
	{
		return JaxbDocument.open( file, JAXBCONTEXT );
	}

	public static JaxbProfileV0 read( String string )
	{
		return JaxbDocument.read( string, JAXBCONTEXT );
	}

	public static void generateSchema( File file )
	{
		JaxbDocument.generateSchema( file, JAXBCONTEXT );
	}

	@Override
	public void write( OutputStream outputStream )
	{
		write( outputStream, JAXBCONTEXT );
	}

	@Override
	public void saveAs( File file )
	{
		saveAs( file, JAXBCONTEXT );
	}

	@Override
	public String toString()
	{
		return toString( JAXBCONTEXT );
	}

	public static JaxbProfileV0 readSemiStructuredDdiProfile( Resource resource )
	{
		JaxbProfileV0 profile = new JaxbProfileV0();
		DomDocument.V13 document = XercesXalanDocument.newBuilder().ofInputStream( resource.readInputStream() ).build();
		for ( Node usedNode : document.selectNodes( "/DDIProfile/Used" ) )
		{
			boolean atLeastOneConstraint = false;
			JaxbConstraintV0 constraint;
			if ( document.selectNode( usedNode, "Description[Content='Required: Mandatory']" ) != null )
			{
				constraint = new JaxbMandatoryNodeConstraintV0( getLocationPath( usedNode ) );
				profile.getConstraints().add( constraint );
				atLeastOneConstraint = true;
			}
			if ( document.selectNode( usedNode, "Description[Content='Required: Recommended']" ) != null
					|| document.selectNode( usedNode, "Description[Content='Recommended']" ) != null )
			{
				constraint = new JaxbRecommendedNodeConstraintV0( getLocationPath( usedNode ) );
				profile.getConstraints().add( constraint );
				atLeastOneConstraint = true;
			}
			if ( document.selectNode( usedNode, "Description[Content='Required: Optional']" ) != null )
			{
				constraint = new JaxbOptionalNodeConstraintV0( getLocationPath( usedNode ) );
				profile.getConstraints().add( constraint );
				atLeastOneConstraint = true;
			}

			if ( !atLeastOneConstraint )
			{
				LOGGER.warn( "usedNode element has really any constraints? \n{}", document.getContent( usedNode ) );
			}
		}
		return profile;
	}

	private static String getLocationPath( org.w3c.dom.Node usedNode )
	{
		return usedNode.getAttributes().getNamedItem( "xpath" ).getTextContent();
	}
}
