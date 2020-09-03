package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.ValidationGateName.BASIC;
import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;

import org.gesis.commons.resource.Resource;
import org.junit.jupiter.api.Test;

import eu.cessda.cmv.core.mediatype.validationreport.v0.ValidationReportV0;

class JdkValidationServiceTest
{
	private CessdaMetadataValidatorFactory factory;

	JdkValidationServiceTest()
	{
		factory = new CessdaMetadataValidatorFactory();
	}

	@Test
	void validateWithUrls() throws MalformedURLException
	{
		ValidationService.V10 validationService = factory.newValidationService();
		URI documentUri = new File( "src/main/resources/demo-documents/ddi-v25/gesis-2800.xml" ).toURI();
		URI profileUri = new File( "src/main/resources/demo-documents/ddi-v25/cdc25_profile.xml" ).toURI();
		ValidationReportV0 validationReport = validationService.validate( documentUri, profileUri, BASIC );
		assertThat( validationReport.getConstraintViolations(), hasSize( 3 ) );
	}

	@Test
	void validateWithResources()
	{
		ValidationService.V10 validationService = factory.newValidationService();
		Resource document = newResource( new File( "src/main/resources/demo-documents/ddi-v25/gesis-2800.xml" ) );
		Resource profile = newResource( new File( "src/main/resources/demo-documents/ddi-v25/cdc25_profile.xml" ) );
		ValidationReportV0 validationReport = validationService.validate( document, profile, BASIC );
		assertThat( validationReport.getConstraintViolations(), hasSize( 3 ) );
	}
}
