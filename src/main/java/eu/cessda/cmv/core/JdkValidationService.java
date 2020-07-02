package eu.cessda.cmv.core;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

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
		ValidationGate.V10 validationGate = factory.newValidationGate( validationGateName );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );
		ValidationReportV0 validationReport = new ValidationReportV0();
		validationReport.setConstraintViolations( constraintViolations.stream()
				.map( ConstraintViolationV0::new )
				.collect( Collectors.toList() ) );
		LOGGER.info( "Validation executed: {}, {}, {}",
				validationGate.getClass().getCanonicalName(),
				documentUrl,
				profileUrl );
		return validationReport;
	}
}
