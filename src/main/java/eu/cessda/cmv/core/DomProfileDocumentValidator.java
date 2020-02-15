package eu.cessda.cmv.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class DomProfileDocumentValidator extends DomMetadataDocumentValidator
{
	public DomProfileDocumentValidator( URI metadataDocumentUri )
	{
		super( metadataDocumentUri, newProfileDocumentUri() );

		getConstraints().add( new PredicatelessXPathConstraint( getMetadataDocument() ) );
		getConstraints().add( new CompilableXPathConstraint( getMetadataDocument() ) );
	}

	private static URI newProfileDocumentUri()
	{
		try
		{
			URL url = DomProfileDocumentValidator.class.getResource( "/cmv-profile-ddi-v32.xml" );
			return url.toURI();
		}
		catch (URISyntaxException e)
		{
			throw new IllegalArgumentException( e );
		}
	}
}
