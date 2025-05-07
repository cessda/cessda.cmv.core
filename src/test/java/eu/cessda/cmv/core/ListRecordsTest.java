package eu.cessda.cmv.core;

import org.junit.jupiter.api.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

class ListRecordsTest
{

	private final CessdaMetadataValidatorFactory factory = new CessdaMetadataValidatorFactory();

	@Test
	void splitListRecordsResponse() throws IOException, SAXException, NotDocumentException
	{
		URL documentURL = this.getClass().getResource( "/demo-documents/ddi-v25/listrecordsresponse.xml");
		assert documentURL != null;

		// Load profile
		URL profileUrl = this.getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" );
		Profile profile = factory.newProfile( profileUrl );

		// Split document
		List<Document> documentList = factory.splitListRecordsResponse( new InputSource( documentURL.toExternalForm() ) );
		assertThat(documentList, hasSize(3));

		// Create validation gate
		ValidationGate validationGate = factory.newValidationGate( ValidationGateName.BASIC );

		// Validate each document
		for (Document doc : documentList) {
			List<ConstraintViolation> validate = validationGate.validate( doc, profile );
			System.out.println(doc.toString() + validate);
		}
	}
}
