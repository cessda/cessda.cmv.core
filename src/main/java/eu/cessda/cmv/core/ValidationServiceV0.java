package eu.cessda.cmv.core;

import java.net.MalformedURLException;
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
		Document document = factory.newDocument( documentUrl );
		Profile profile = factory.newProfile( profileUrl );
		return validate( document, documentUrl, profile, profileUrl, validationGateName );
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public ValidationReportV0 validate(
			Resource documentResource,
			Resource profileResource,
			ValidationGateName validationGateName )
	{
		try
		{
			Document document = factory.newDocument( documentResource );
			Profile profile = factory.newProfile( profileResource );
			return validate( document, documentResource.getUri().toURL(),
					profile, profileResource.getUri().toURL(),
					validationGateName );
		}
		catch (MalformedURLException e)
		{
			throw new IllegalArgumentException( e );
		}
	}

	private ValidationReportV0 validate(
			Document document,
			URL documentUrl,
			Profile profile,
			URL profileUrl,
			ValidationGateName validationGateName )
	{
		ValidationGate.V10 validationGate = factory.newValidationGate( validationGateName );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		ValidationReportV0 validationReport = new ValidationReportV0();
		validationReport.setDocumentUrl( documentUrl );
		validationReport.setConstraintViolations( constraintViolations.stream()
				.map( ConstraintViolationV0::new )
				.collect( Collectors.toList() ) );
		LOGGER.info( "Validation executed: {}, {}, {}", validationGateName, documentUrl, profileUrl );
		return validationReport;
	}
}
