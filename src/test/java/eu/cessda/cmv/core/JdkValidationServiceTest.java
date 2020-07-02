package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.ValidationGateName.BASIC;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Test;

import eu.cessda.cmv.core.mediatype.validationreport.v0.ValidationReportV0;

public class JdkValidationServiceTest
{
	private CessdaMetadataValidatorFactory factory;

	public JdkValidationServiceTest()
	{
		factory = new CessdaMetadataValidatorFactory();
	}

	@Test
	public void validate() throws MalformedURLException
	{
		ValidationService.V10 validationService = factory.newValidationService();
		URL documentUrl = new File( "src/main/resources/demo-documents/ddi-v25/gesis-2800.xml" ).toURI().toURL();
		URL profileUrl = new File( "src/main/resources/demo-documents/ddi-v25/cdc25_profile.xml" ).toURI().toURL();
		ValidationReportV0 validationReport = validationService.validate( documentUrl, profileUrl, BASIC );
		assertThat( validationReport.getConstraintViolations(), hasSize( 9 ) );
	}
}
