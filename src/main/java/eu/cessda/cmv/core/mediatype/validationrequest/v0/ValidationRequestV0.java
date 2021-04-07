package eu.cessda.cmv.core.mediatype.validationrequest.v0;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.NoSuchFileException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.gesis.commons.xml.jaxb.DefaultNamespacePrefixMapper;
import org.gesis.commons.xml.jaxb.JaxbDocument;
import org.gesis.commons.xml.jaxb.NamespacePrefixMapper;

import eu.cessda.cmv.core.ValidationRequest;

@XmlRootElement( name = ValidationRequestV0.VALIDATIONREQUEST_ELEMENT )
@XmlType( name = ValidationRequestV0.VALIDATIONREQUEST_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class ValidationRequestV0 extends JaxbDocument implements ValidationRequest
{
	static final String MAJOR = "0";
	static final String MINOR = "1";
	static final String VERSION = MAJOR + "." + MINOR;
	public static final String MEDIATYPE = "application/vnd.eu.cessda.cmv.core.mediatype.validation-request.v" + VERSION
			+ "+xml";
	static final String SCHEMALOCATION_HOST = "https://bitbucket.org/cessda/cessda.cmv.core/src/raw/stable/schema";
	public static final String SCHEMALOCATION_FILENAME = "validation-request-v" + VERSION + ".xsd";

	static final String NAMESPACE_DEFAULT_PREFIX = "";
	static final String NAMESPACE_DEFAULT_URI = "cmv:validation-request:v" + MAJOR;
	static final String SCHEMALOCATION_URI = SCHEMALOCATION_HOST + "/" + SCHEMALOCATION_FILENAME;
	static final String SCHEMALOCATION = NAMESPACE_DEFAULT_URI + " " + SCHEMALOCATION_URI;

	static final String VALIDATIONREQUEST_ELEMENT = "ValidationRequest";
	static final String VALIDATIONREQUEST_TYPE = VALIDATIONREQUEST_ELEMENT + "Type";
	static final String DOCUMENT_ELEMENT = "Document";
	static final String DOCUMENT_TYPE = DOCUMENT_ELEMENT + "Type";
	static final String PROFILE_ELEMENT = "Profile";
	static final String PROFILE_TYPE = PROFILE_ELEMENT + "Type";

	private static final JAXBContext JAXBCONTEXT = newJaxbContext( ValidationRequestV0.class );

	@XmlElement( name = DOCUMENT_ELEMENT )
	private DocumentV0 document;

	@XmlElement( name = PROFILE_ELEMENT )
	private DocumentV0 profile;

	protected ValidationRequestV0( String schemaLocation, NamespacePrefixMapper namespacePrefixMapper )
	{
		super( schemaLocation, namespacePrefixMapper );
	}

	public ValidationRequestV0()
	{
		super( SCHEMALOCATION, new DefaultNamespacePrefixMapper( NAMESPACE_DEFAULT_URI ) );
	}

	public DocumentV0 getDocument()
	{
		return document;
	}

	public void setDocument( DocumentV0 document )
	{
		this.document = document;
	}

	public DocumentV0 getProfile()
	{
		return profile;
	}

	public void setProfile( DocumentV0 profile )
	{
		this.profile = profile;
	}

	public static ValidationRequestV0 read( InputStream inputStream )
	{
		return JaxbDocument.read( inputStream, JAXBCONTEXT );
	}

	public static ValidationRequestV0 open( File file ) throws NoSuchFileException
	{
		return JaxbDocument.open( file, JAXBCONTEXT );
	}

	public static ValidationRequestV0 read( String string )
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
}
