package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.Factory.newDomDocument;
import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.gesis.commons.xml.DomDocument;
import org.gesis.commons.xml.ddi.DdiInputStream;
import org.junit.jupiter.api.Test;

public class CodeValueOfControlledVocabularyContraintTest
{
	private TestEnv.V13 testEnv = DefaultTestEnv.newInstance( CodeValueOfControlledVocabularyContraintTest.class );

	@Test
	public void validate_invalid() throws IOException
	{
		// given
		URL documentFile = testEnv.findTestResourceByName( "invalid.xml" ).toURI().toURL();
		URL profileFile = testEnv.findTestResourceByName( "profile.xml" ).toURI().toURL();

		try ( InputStream documentInputStream = newResource( documentFile ).readInputStream();
				InputStream profileInputStream = newResource( profileFile ).readInputStream() )
		{
			// when
			DomDocument.V11 metadataDocument = newDomDocument( new DdiInputStream( documentInputStream ) );
			DomDocument.V11 profileDocument = newDomDocument( new DdiInputStream( profileInputStream ) );

			// then
			Constraint.V10 constraint = new CodeValueOfControlledVocabularyContraint( metadataDocument,
					profileDocument );
			List<ConstraintViolation.V10> constraintViolations = constraint.validate();

			constraintViolations.stream().map( ConstraintViolation.V10::getMessage ).forEach(
					System.out::println );

			// then
			assertThat( constraintViolations, hasSize( 1 ) );
		}
	}

	@Test
	public void validate_valid() throws IOException
	{
		// given
		URL documentFile = testEnv.findTestResourceByName( "valid.xml" ).toURI().toURL();
		URL profileFile = testEnv.findTestResourceByName( "profile.xml" ).toURI().toURL();

		try ( InputStream documentInputStream = newResource( documentFile ).readInputStream();
				InputStream profileInputStream = newResource( profileFile ).readInputStream() )
		{
			// when
			DomDocument.V11 metadataDocument = newDomDocument( new DdiInputStream( documentInputStream ) );
			DomDocument.V11 profileDocument = newDomDocument( new DdiInputStream( profileInputStream ) );

			// then
			Constraint.V10 constraint = new CodeValueOfControlledVocabularyContraint( metadataDocument,
					profileDocument );
			List<ConstraintViolation.V10> constraintViolations = constraint.validate();

			// then
			assertThat( constraintViolations, hasSize( 0 ) );
		}
	}

	@Test
	public void test() throws IOException
	{
		URL documentFile = testEnv.findTestResourceByName( "invalid.xml" ).toURI().toURL();

		try ( InputStream documentInputStream = newResource( documentFile ).readInputStream() )
		{
			DomDocument.V11 metadataDocument = newDomDocument( new DdiInputStream( documentInputStream ) );
			metadataDocument.selectNodes( "/codeBook/stdyDscr/stdyInfo/sumDscr/anlyUnit/concept" ).stream()
					.forEach( n ->
					{
						System.out.println( n.getTextContent() + " " + n.getUserData( "lineNumber" ) );

					} );
		}
	}
}
