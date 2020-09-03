package eu.cessda.cmv.core.mediatype.validationreport.v0;

import static eu.cessda.cmv.core.ValidationGateName.STANDARD;
import static eu.cessda.cmv.core.mediatype.validationreport.v0.ValidationReportV0.SCHEMALOCATION_FILENAME;
import static org.gesis.commons.test.hamcrest.FileMatchers.hasEqualContent;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import eu.cessda.cmv.core.CessdaMetadataValidatorFactory;
import eu.cessda.cmv.core.ValidationService;

class ValidationReportV0Test
{
	private TestEnv.V11 testEnv;
	private CessdaMetadataValidatorFactory factory = new CessdaMetadataValidatorFactory();

	ValidationReportV0Test()
	{
		testEnv = DefaultTestEnv.newInstance( ValidationReportV0Test.class );
	}

	@Test
	void generateSchema() throws IOException
	{
		File expectedFile = testEnv.findTestResourceByName( SCHEMALOCATION_FILENAME );
		File actualFile = new File( testEnv.newDirectory(), SCHEMALOCATION_FILENAME );

		ValidationReportV0.generateSchema( actualFile );

		assertThat( actualFile, hasEqualContent( expectedFile ) );
	}

	@Test
	@Disabled
	void printOutToString() throws Exception
	{
		assertThat( ValidationReportV0.MEDIATYPE, Matchers.equalTo( ValidationReportV0.MEDIATYPE ) );

		URI documentUri = getClass().getResource( "/demo-documents/ddi-v25/ukds-7481.xml" ).toURI();
		URI profileUri = getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" ).toURI();

		ValidationService.V10 validationService = factory.newValidationService();
		ValidationReportV0 validationReport = validationService.validate( documentUri, profileUri, STANDARD );
		System.out.println( validationReport.toString() );
	}
}
