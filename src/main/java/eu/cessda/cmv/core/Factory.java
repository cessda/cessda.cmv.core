package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;
import static org.gesis.commons.resource.Resource.newResource;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.xml.DomDocument;
import org.gesis.commons.xml.XercesXalanDocument;

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
}
