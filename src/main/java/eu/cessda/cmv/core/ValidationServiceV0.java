package eu.cessda.cmv.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.gesis.commons.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.cessda.cmv.core.mediatype.validationreport.v0.ConstraintViolationV0;
import eu.cessda.cmv.core.mediatype.validationreport.v0.ValidationReportV0;

class ValidationServiceV0 implements ValidationService.V10
{
	private static final Logger LOGGER = LoggerFactory.getLogger( ValidationServiceV0.class );

	private CessdaMetadataValidatorFactory factory;

	ValidationServiceV0( CessdaMetadataValidatorFactory factory )
	{
		this.factory = factory;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public ValidationReportV0 validate(
			URL documentUrl,
			URL profileUrl,
			ValidationGateName validationGateName )
	{
		try
		{
			Document document = factory.newDocument( documentUrl );
			Profile profile = factory.newProfile( profileUrl );
			return validate( document,
					documentUrl.toURI(),
					profile,
					profileUrl.toURI(),
					validationGateName );
		}
		catch (URISyntaxException e)
		{
			throw new IllegalArgumentException( e );
		}
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public ValidationReportV0 validate(
			Resource documentResource,
			Resource profileResource,
			ValidationGateName validationGateName )
	{
		Document document = factory.newDocument( documentResource );
		Profile profile = factory.newProfile( profileResource );
		return validate( document,
				documentResource.getUri(),
				profile,
				profileResource.getUri(),
				validationGateName );
	}

	private ValidationReportV0 validate(
			Document document,
			URI documentUri,
			Profile profile,
			URI profileUri,
			ValidationGateName validationGateName )
	{
		ValidationGate.V10 validationGate = factory.newValidationGate( validationGateName );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		ValidationReportV0 validationReport = new ValidationReportV0();
		validationReport.setConstraintViolations( constraintViolations.stream()
				.map( ConstraintViolationV0::new )
				.collect( Collectors.toList() ) );
		LOGGER.info( "Validation executed: {}, {}, {}", validationGateName, documentUri, profileUri );
		return validationReport;
	}
}
