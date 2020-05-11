package eu.cessda.cmv.core;

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

class Factory
{
	private Factory()
	{
	}

	public static DomDocument.V11 newDomDocument( File file )
	{
		requireNonNull( file );
		return newDomDocument( file.toURI() );
	}

	public static DomDocument.V11 newDomDocument( URI uri )
	{
		requireNonNull( uri );
		Resource resource = newResource( uri );
		return newDomDocument( resource.readInputStream() );
	}

	public static DomDocument.V11 newDomDocument( InputStream inputStream )
	{
		requireNonNull( inputStream );
		return XercesXalanDocument.newBuilder()
				.ofInputStream( inputStream )
				.namespaceUnaware()
				.printPrettyWithIndentation( 2 )
				.build();
	}

	public static Document.V10 newDocument( File file )
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

	public static Document.V10 newDocument( URL url )
	{
		return new eu.cessda.cmv.core.DomCodebookDocument( newDdiInputStream( url ) );
	}

	public static Profile.V10 newProfile( URL url )
	{
		return new eu.cessda.cmv.core.DomDdiProfile( newDdiInputStream( url ) );
	}

	public static DdiInputStream newDdiInputStream( URL url )
	{
		requireNonNull( url );
		Resource resource = newResource( url );
		return newDdiInputStream( resource.readInputStream() );
	}

	public static DdiInputStream newDdiInputStream( InputStream inputStream )
	{
		return new DdiInputStream( inputStream );
	}
}
