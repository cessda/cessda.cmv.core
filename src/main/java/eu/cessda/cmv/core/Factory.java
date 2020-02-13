package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.net.URI;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;
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
		Resource resource = new TextResource( Resource.newResource( uri ) );
		return XercesXalanDocument.newBuilder()
				.ofContent( resource.toString() )
				.namespaceUnaware()
				.printPrettyWithIndentation( 2 )
				.build();
	}
}
