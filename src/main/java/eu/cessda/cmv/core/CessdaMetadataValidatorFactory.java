package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.ValidationGateName.BASIC;
import static eu.cessda.cmv.core.ValidationGateName.BASICPLUS;
import static eu.cessda.cmv.core.ValidationGateName.EXTENDED;
import static eu.cessda.cmv.core.ValidationGateName.STANDARD;
import static eu.cessda.cmv.core.ValidationGateName.STRICT;
import static java.util.Objects.requireNonNull;
import static org.gesis.commons.resource.Resource.newResource;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.xml.DomDocument;
import org.gesis.commons.xml.XercesXalanDocument;
import org.gesis.commons.xml.ddi.DdiInputStream;

public class CessdaMetadataValidatorFactory
{
	public DomDocument.V11 newDomDocument( File file )
	{
		requireNonNull( file );
		return newDomDocument( file.toURI() );
	}

	public DomDocument.V11 newDomDocument( URI uri )
	{
		requireNonNull( uri );
		Resource resource = newResource( uri );
		return newDomDocument( resource.readInputStream() );
	}

	public DomDocument.V11 newDomDocument( InputStream inputStream )
	{
		requireNonNull( inputStream );
		return XercesXalanDocument.newBuilder()
				.ofInputStream( inputStream )
				.namespaceUnaware()
				.printPrettyWithIndentation( 2 )
				.build();
	}

	public Document.V11 newDocument( File file )
	{
		try
		{
			requireNonNull( file );
			return newDocument( file.toURI().toURL() );
		}
		catch (MalformedURLException e)
		{
			throw new IllegalArgumentException( e );
		}
	}

	public Document.V11 newDocument( URL url )
	{
		return new eu.cessda.cmv.core.DomCodebookDocument( newDdiInputStream( url ) );
	}

	public Profile.V10 newProfile( URL url )
	{
		return new DomSemiStructuredDdiProfile( newDdiInputStream( url ) );
	}

	public Profile.V10 newProfile( File file )
	{
		try
		{
			return newProfile( file.toURI().toURL() );
		}
		catch (MalformedURLException e)
		{
			throw new IllegalArgumentException( e );
		}
	}

	public DdiInputStream newDdiInputStream( URL url )
	{
		requireNonNull( url );
		Resource resource = newResource( url );
		return newDdiInputStream( resource.readInputStream() );
	}

	public DdiInputStream newDdiInputStream( InputStream inputStream )
	{
		return new DdiInputStream( inputStream );
	}

	public ValidationGate.V10 newValidationGate( ValidationGateName name )
	{
		requireNonNull( name );
		if ( name.equals( BASIC ) )
		{
			return new BasicValidationGate();
		}
		else if ( name.equals( BASICPLUS ) )
		{
			return new BasicPlusValidationGate();
		}
		else if ( name.equals( STANDARD ) )
		{
			return new StandardValidationGate();
		}
		else if ( name.equals( EXTENDED ) )
		{
			return new ExtendedValidationGate();
		}
		else if ( name.equals( STRICT ) )
		{
			return new StrictValidationGate();
		}
		else
		{
			throw new IllegalArgumentException( name + " not supported" );
		}
	}

	public ValidationService.V10 newValidationService()
	{
		return new JdkValidationService( this );
	}
}
