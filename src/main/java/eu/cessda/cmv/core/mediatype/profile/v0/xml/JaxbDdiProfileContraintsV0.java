package eu.cessda.cmv.core.mediatype.profile.v0.xml;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;
import static javax.xml.bind.Marshaller.JAXB_FRAGMENT;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.gesis.commons.xml.jaxb.JaxbException;
import org.gesis.commons.xml.jaxb.NotParseableException;

@XmlRootElement( name = "Constraints" )
public class JaxbDdiProfileContraintsV0
{
	@XmlElement( name = JaxbConstraintV0.JAXB_ELEMENT )
	private List<JaxbConstraintV0> constraints = new ArrayList<>();

	public List<JaxbConstraintV0> getConstraints()
	{
		return constraints;
	}

	public static JaxbDdiProfileContraintsV0 fromString( String xml )
	{
		try
		{
			return read( xml, JAXBContext.newInstance( JaxbDdiProfileContraintsV0.class ) );
		}
		catch (JAXBException e)
		{
			throw new IllegalArgumentException( e );
		}
	}

	private static JaxbDdiProfileContraintsV0 read( String string, JAXBContext jaxbContext )
	{
		requireNonNull( string );
		try ( InputStream inputStream = new ByteArrayInputStream( string.getBytes( UTF_8 ) ) )
		{
			return read( inputStream, jaxbContext );
		}
		catch (IOException e)
		{
			throw new JaxbException( e );
		}
	}

	@SuppressWarnings( "unchecked" )
	protected static JaxbDdiProfileContraintsV0 read( InputStream inputStream, JAXBContext jaxbContext )
	{
		requireNonNull( inputStream );
		requireNonNull( jaxbContext );
		try
		{
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			return (JaxbDdiProfileContraintsV0) unmarshaller.unmarshal( inputStream );
		}
		catch (UnmarshalException e)
		{
			throw new NotParseableException( e );
		}
		catch (JAXBException e)
		{
			throw new JaxbException( e );
		}
	}

	public String toString()
	{
		try ( ByteArrayOutputStream outputStream = new ByteArrayOutputStream() )
		{
			JAXBContext jaxbContext = JAXBContext.newInstance( JaxbDdiProfileContraintsV0.class );
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty( JAXB_FORMATTED_OUTPUT, true );
			marshaller.setProperty( JAXB_FRAGMENT, Boolean.TRUE );
			marshaller.marshal( this, outputStream );
			return new String( outputStream.toByteArray() );
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException( e );
		}
	}
}
