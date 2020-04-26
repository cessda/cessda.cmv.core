package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.JaxbValidationReport.VALIDATIONREPORT_ELEMENT;
import static eu.cessda.cmv.core.JaxbValidationReport.VALIDATIONREPORT_TYPE;

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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.gesis.commons.xml.jaxb.DefaultNamespacePrefixMapper;
import org.gesis.commons.xml.jaxb.JaxbDocument;
import org.gesis.commons.xml.jaxb.NamespacePrefixMapper;

@XmlRootElement( name = VALIDATIONREPORT_ELEMENT )
@XmlType( name = VALIDATIONREPORT_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
class JaxbValidationReport extends JaxbDocument
{
	static final String MAJOR = "0";
	static final String MINOR = "1";
	static final String VERSION = MAJOR + "." + MINOR;
	static final String MEDIATYPE = "application/vnd.eu.cessda.cmv.validation-report.v" + VERSION + " +xml";
	static final String SCHEMALOCATION_HOST = "https://bitbucket.org/cessda/cessda.cmv.core/src/raw/stable/schema";
	public static final String SCHEMALOCATION_FILENAME = "validation-report-v" + VERSION + ".xsd";
	static final String NAMESPACE_DEFAULT_PREFIX = "vr";
	static final String NAMESPACE_DEFAULT_URI = "cmv:validation-report:v" + MAJOR;
	static final String SCHEMALOCATION_URI = SCHEMALOCATION_HOST + "/" + SCHEMALOCATION_FILENAME;
	static final String SCHEMALOCATION = NAMESPACE_DEFAULT_URI + " " + SCHEMALOCATION_URI;
	static final String VALIDATIONREPORT_ELEMENT = "ValidationReport";
	static final String VALIDATIONREPORT_TYPE = VALIDATIONREPORT_ELEMENT + "Type";
	static final String CONSTRAINTVIOLATION_ELEMENT = "ConstraintViolation";
	static final String CONSTRAINTVIOLATION_TYPE = CONSTRAINTVIOLATION_ELEMENT + "Type";

	private static final JAXBContext JAXBCONTEXT = newJaxbContext( JaxbValidationReport.class );

	@XmlElement( name = CONSTRAINTVIOLATION_ELEMENT )
	private List<ConstraintViolation> constraintViolations;

	protected JaxbValidationReport( String schemaLocation, NamespacePrefixMapper namespacePrefixMapper )
	{
		super( schemaLocation, namespacePrefixMapper );
	}

	public JaxbValidationReport()
	{
		super( SCHEMALOCATION, new DefaultNamespacePrefixMapper( NAMESPACE_DEFAULT_URI ) );
		constraintViolations = new ArrayList<>();
	}

	public List<ConstraintViolation> getConstraintViolations()
	{
		return constraintViolations;
	}

	public void setConstraintViolations( List<ConstraintViolation> constraintViolations )
	{
		this.constraintViolations = constraintViolations;
	}

	public static JaxbValidationReport read( InputStream inputStream )
	{
		return JaxbDocument.read( inputStream, JAXBCONTEXT );
	}

	public static JaxbValidationReport open( File file ) throws NoSuchFileException
	{
		return JaxbDocument.open( file, JAXBCONTEXT );
	}

	public static JaxbValidationReport read( String string )
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
