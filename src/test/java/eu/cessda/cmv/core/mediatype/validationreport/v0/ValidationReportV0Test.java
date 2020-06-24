package eu.cessda.cmv.core.mediatype.validationreport.v0;

import static eu.cessda.cmv.core.ValidationGateName.STANDARD;
import static eu.cessda.cmv.core.mediatype.validationreport.v0.ValidationReportV0.SCHEMALOCATION_FILENAME;
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
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import eu.cessda.cmv.core.CessdaMetadataValidatorFactory;
import eu.cessda.cmv.core.ConstraintViolation;
import eu.cessda.cmv.core.Document;
import eu.cessda.cmv.core.DomCodebookDocument;
import eu.cessda.cmv.core.DomSemiStructuredDdiProfile;
import eu.cessda.cmv.core.Profile;
import eu.cessda.cmv.core.ValidationGate;

public class ValidationReportV0Test
{
	private TestEnv.V11 testEnv;
	private CessdaMetadataValidatorFactory factory = new CessdaMetadataValidatorFactory();

	public ValidationReportV0Test()
	{
		testEnv = DefaultTestEnv.newInstance( ValidationReportV0Test.class );
	}

	@Test
	public void generateSchema() throws IOException
	{
		File expectedFile = testEnv.findTestResourceByName( SCHEMALOCATION_FILENAME );
		File actualFile = new File( testEnv.newDirectory(), SCHEMALOCATION_FILENAME );

		ValidationReportV0.generateSchema( actualFile );
		// System.out.println( new String( readAllBytes( actualFile.toPath() ) ) );

		assertThat( actualFile, hasEqualContent( expectedFile ) );
	}

	@Test
	public void printOutToString() throws IOException
	{
		assertThat( ValidationReportV0.MEDIATYPE, Matchers.equalTo( ValidationReportV0.MEDIATYPE ) );

		Document document = newDocument( getClass().getResource( "/demo-documents/ddi-v25/ukds-7481.xml" ) );
		Profile profile = newProfile( getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" ) );

		ValidationGate.V10 validationGate = factory.newValidationGate( STANDARD );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );
		ValidationReportV0 validationReport = new ValidationReportV0();
		validationReport.setConstraintViolations( constraintViolations.stream()
				.map( ConstraintViolationV0::new )
				.collect( Collectors.toList() ) );

		// System.out.println( validationReport.toString() );
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
		return new DomSemiStructuredDdiProfile( inputStream );
	}
}
