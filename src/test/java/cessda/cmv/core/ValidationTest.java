package cessda.cmv.core;

import static org.gesis.commons.resource.Resource.newResource;

import java.io.File;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;
import org.gesis.commons.xml.DomDocument;
import org.gesis.commons.xml.XercesXalanDocument;
import org.junit.jupiter.api.Test;

public class ValidationTest
{
	@Test
	public void test()
	{
		File file = new File( "src/test/resources/ddi-v25/ukds-7481.xml" );
		Resource resource = new TextResource( newResource( file.toURI() ) );
		DomDocument.V10 document = XercesXalanDocument.newBuilder()
				.ofContent( resource.toString() )
				.build();
		System.out.println( document.getContent() );
	}
}
