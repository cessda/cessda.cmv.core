package eu.cessda.cmv.core;

import java.io.File;
import java.net.URI;

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
		// TODO: Read profile from classpath in compiled jar (#38)
		return new File( "src/main/resources/cmv-profile-ddi-v32.xml" ).toURI();
	}
}
