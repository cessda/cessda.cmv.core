package eu.cessda.cmv.core.mediatype.validationreport.v0.xml;

import static eu.cessda.cmv.core.mediatype.validationreport.v0.xml.JaxbValidationReportV0.SCHEMALOCATION_FILENAME;
import static java.nio.file.Files.readAllBytes;
import static java.util.Objects.requireNonNull;
import static org.gesis.commons.resource.Resource.newResource;
import static org.gesis.commons.test.hamcrest.FileMatchers.hasEqualContent;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.gesis.commons.xml.ddi.DdiInputStream;
import org.junit.jupiter.api.Test;

import eu.cessda.cmv.core.ConstraintViolation;
import eu.cessda.cmv.core.Document;
import eu.cessda.cmv.core.DomCodebookDocument;
import eu.cessda.cmv.core.DomProfile;
import eu.cessda.cmv.core.Profile;
import eu.cessda.cmv.core.StandardValidationGate;
import eu.cessda.cmv.core.ValidationGate;

public class JaxbValidationReportV0Test
{
	private TestEnv.V11 testEnv;

	public JaxbValidationReportV0Test()
	{
		testEnv = DefaultTestEnv.newInstance( JaxbValidationReportV0Test.class );
	}

	@Test
	public void generateSchema() throws IOException
	{
		File expectedFile = testEnv.findTestResourceByName( SCHEMALOCATION_FILENAME );
		File actualFile = new File( testEnv.newDirectory(), SCHEMALOCATION_FILENAME );

		JaxbValidationReportV0.generateSchema( actualFile );
		System.out.println( new String( readAllBytes( actualFile.toPath() ) ) );

		assertThat( actualFile, hasEqualContent( expectedFile ) );
	}

	@Test
	public void printOutToString() throws IOException
	{
		Document document = newDocument( getClass().getResource( "/demo-documents/ddi-v25/ukds-7481.xml" ) );
		Profile profile = newProfile( getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" ) );

		ValidationGate.V10 validationGate = new StandardValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );
		JaxbValidationReportV0 validationReport = new JaxbValidationReportV0();
		validationReport.setConstraintViolations( constraintViolations.stream()
				.map( JaxbConstraintViolationV0::new )
				.collect( Collectors.toList() ) );

		System.out.println( validationReport.toString() );
		System.out.println( JaxbValidationReportV0.MEDIATYPE );
	}

	private static Document.V10 newDocument( URL url )
	{
		// TODO Replace by public factory, see eu.cessda.cmv.core.Factory

		requireNonNull( url );
		Resource resource = newResource( url );
		DdiInputStream inputStream = new DdiInputStream( resource.readInputStream() );
		return new DomCodebookDocument( inputStream );
	}

	private static Profile.V10 newProfile( URL url )
	{
		// TODO Replace by public factory, see eu.cessda.cmv.core.Factory

		requireNonNull( url );
		Resource resource = newResource( url );
		DdiInputStream inputStream = new DdiInputStream( resource.readInputStream() );
		return new DomProfile( inputStream );
	}
}
