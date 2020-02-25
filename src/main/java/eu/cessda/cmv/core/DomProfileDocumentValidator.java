package eu.cessda.cmv.core;

import java.io.InputStream;
import java.net.URL;

import org.gesis.commons.resource.Resource;

public class DomProfileDocumentValidator extends DomMetadataDocumentValidator
{
	public DomProfileDocumentValidator( InputStream metadataDocumentInputStream )
	{
		super( metadataDocumentInputStream, newProfileDocumentInputStream() );

		getConstraints().add( new PredicatelessXPathConstraint( getMetadataDocument() ) );
		getConstraints().add( new CompilableXPathConstraint( getMetadataDocument() ) );
	}

	private static InputStream newProfileDocumentInputStream()
	{

		URL url = DomProfileDocumentValidator.class.getResource( "/cmv-profile-ddi-v32.xml" );
		Resource resource = Resource.newResource( url );
		return resource.readInputStream();
	}
}
