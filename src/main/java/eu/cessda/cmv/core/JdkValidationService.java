package eu.cessda.cmv.core;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.gesis.commons.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.cessda.cmv.core.mediatype.validationreport.v0.ConstraintViolationV0;
import eu.cessda.cmv.core.mediatype.validationreport.v0.ValidationReportV0;

class JdkValidationService implements ValidationService.V10
{
	private static final Logger LOGGER = LoggerFactory.getLogger( JdkValidationService.class );

	private CessdaMetadataValidatorFactory factory;

	JdkValidationService( CessdaMetadataValidatorFactory factory )
	{
		this.factory = factory;
	}

	@Override
	public ValidationReportV0 validate(
			URL documentUrl,
			URL profileUrl,
			ValidationGateName validationGateName )
	{
		Document document = factory.newDocument( documentUrl );
		Profile profile = factory.newProfile( profileUrl );
		ValidationReportV0 validationReport = validate( document, profile, validationGateName );
		LOGGER.info( "Validation executed: {}, {}, {}",
				validationGateName, documentUrl, profileUrl );
		return validationReport;
	}

	@Override
	public ValidationReportV0 validate(
			Resource documentResource,
			Resource profileResource,
			ValidationGateName validationGateName )
	{
		Document document = factory.newDocument( documentResource );
		Profile profile = factory.newProfile( profileResource );
		ValidationReportV0 validationReport = validate( document, profile, validationGateName );
		LOGGER.info( "Validation executed: {}, {}, {}",
				validationGateName, documentResource.getUri(), profileResource.getUri() );
		return validationReport;
	}

	private ValidationReportV0 validate(
			Document document,
			Profile profile,
			ValidationGateName validationGateName )
	{
		ValidationGate.V10 validationGate = factory.newValidationGate( validationGateName );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );
		ValidationReportV0 validationReport = new ValidationReportV0();
		validationReport.setConstraintViolations( constraintViolations.stream()
				.map( ConstraintViolationV0::new )
				.collect( Collectors.toList() ) );
		return validationReport;
	}
}
