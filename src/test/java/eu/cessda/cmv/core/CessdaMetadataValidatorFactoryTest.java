package eu.cessda.cmv.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;

import org.gesis.commons.xml.DomDocument;
import org.junit.jupiter.api.Test;

class CessdaMetadataValidatorFactoryTest
{
	private CessdaMetadataValidatorFactory factory;

	CessdaMetadataValidatorFactoryTest()
	{
		factory = new CessdaMetadataValidatorFactory();
	}

	@Test
	void newDomDocument()
	{
		File file = new File( "src/main/resources/cmv-profile-ddi-v32.xml" );
		DomDocument.V11 document = factory.newDomDocument( file );
		assertThat( document.selectNode( "/pr:DDIProfile" ), notNullValue() );
	}
}
