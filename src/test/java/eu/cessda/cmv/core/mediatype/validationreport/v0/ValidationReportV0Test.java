package eu.cessda.cmv.core.mediatype.validationreport.v0;

import static eu.cessda.cmv.core.ValidationGateName.STANDARD;
import static eu.cessda.cmv.core.mediatype.validationreport.v0.ValidationReportV0.SCHEMALOCATION_FILENAME;
import static org.gesis.commons.test.hamcrest.FileMatchers.hasEqualContent;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import eu.cessda.cmv.core.CessdaMetadataValidatorFactory;
import eu.cessda.cmv.core.ValidationService;

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
		System.out.println( actualFile );

		assertThat( actualFile, hasEqualContent( expectedFile ) );
	}

	@Test
	@Disabled
	public void printOutToString() throws IOException
	{
		assertThat( ValidationReportV0.MEDIATYPE, Matchers.equalTo( ValidationReportV0.MEDIATYPE ) );

		URL documentUrl = getClass().getResource( "/demo-documents/ddi-v25/ukds-7481.xml" );
		URL profileUrl = getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" );

		ValidationService.V10 validationService = factory.newValidationService();
		ValidationReportV0 validationReport = validationService.validate( documentUrl, profileUrl, STANDARD );
		System.out.println( validationReport.toString() );
	}
}
