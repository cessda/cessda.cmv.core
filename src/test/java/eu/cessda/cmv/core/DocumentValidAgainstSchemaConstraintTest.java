package eu.cessda.cmv.core;

import static org.gesis.commons.test.hamcrest.OptionalMatchers.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.gesis.commons.xml.XmlNotValidAgainstSchemaException;
import org.gesis.commons.xml.XmlSchemaNotFoundException;
import org.junit.jupiter.api.Test;

class DocumentValidAgainstSchemaConstraintTest
{
	private TestEnv.V14 testEnv;
	private CessdaMetadataValidatorFactory factory;

	DocumentValidAgainstSchemaConstraintTest()
	{
		testEnv = DefaultTestEnv.newInstance( DocumentValidAgainstSchemaConstraintTest.class );
		factory = new CessdaMetadataValidatorFactory();
	}

	@Test
	void isValid()
	{
		File file = testEnv.findTestResourceByName( "88-document-valid.xml" );
		Document.V12 document = (Document.V12) factory.newDocument( file );
		DocumentValidAgainstSchemaValidator validator = new DocumentValidAgainstSchemaValidator( document );

		assertThat( validator.validate(), isEmpty() );
	}

	@Test
	void isInvalid_element()
	{
		File file = testEnv.findTestResourceByName( "88-document-invalid-1.xml" );
		Document.V12 document = (Document.V12) factory.newDocument( file );
		assertThrows( XmlNotValidAgainstSchemaException.class, () -> document.validate() );

		DocumentValidAgainstSchemaValidator validator = new DocumentValidAgainstSchemaValidator( document );
		assertThat( validator.validate().get().getMessage(),
				equalTo( "Document is not valid against schema with 1 violations" ) );
	}

	@Test
	void isInvalid_schemaNotFound()
	{
		File file = testEnv.findTestResourceByName( "88-document-invalid-2.xml" );
		Document.V12 document = (Document.V12) factory.newDocument( file );
		assertThrows( XmlSchemaNotFoundException.class, () -> document.validate() );

		DocumentValidAgainstSchemaValidator validator = new DocumentValidAgainstSchemaValidator( document );
		assertThat( validator.validate().get().getMessage(),
				equalTo( "XML Schema location is missing" ) );

	}
}
