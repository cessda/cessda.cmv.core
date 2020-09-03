package eu.cessda.cmv.core;

import static org.gesis.commons.resource.Resource.newResource;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.URL;

import org.gesis.commons.xml.XmlNotWellformedException;
import org.gesis.commons.xml.ddi.DdiInputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class DomCodebookDocumentTest
{
	@Test
	void constructWithNotWellformedDocument() throws IOException
	{
		// given
		URL url = getClass().getResource( "/demo-documents/ddi-v25/ukds-7481-not-wellformed.xml-invalid" );
		try ( DdiInputStream documentInputStream = new DdiInputStream( newResource( url ).readInputStream() ) )
		{
			// when
			Executable executable = () -> new DomCodebookDocument( documentInputStream );
			// then
			assertThrows( XmlNotWellformedException.class, executable );
		}
	}
}
