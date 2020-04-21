package eu.cessda.cmv.core;

import static org.gesis.commons.resource.Resource.newResource;

import java.io.InputStream;
import java.net.URL;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.xml.ddi.DdiInputStream;

public class DomProfileDocumentValidationGate extends DomMetadataDocumentValidationGate
{
	public DomProfileDocumentValidationGate( InputStream metadataDocumentInputStream )
	{
		super( new DdiInputStream( metadataDocumentInputStream ),
				new DdiInputStream( newProfileDocumentInputStream() ) );

		getConstraints().add( new PredicatelessXPathConstraint( getMetadataDocument() ) );
		getConstraints().add( new CompilableXPathConstraint( getMetadataDocument() ) );
	}

	private static InputStream newProfileDocumentInputStream()
	{
		URL url = DomProfileDocumentValidationGate.class.getResource( "/cmv-profile-ddi-v32.xml" );
		Resource resource = newResource( url );
		return resource.readInputStream();
	}
}
