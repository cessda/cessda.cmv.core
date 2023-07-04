/*-
 * #%L
 * cmv-core
 * %%
 * Copyright (C) 2020 - 2021 CESSDA ERIC
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package eu.cessda.cmv.core;

import eu.cessda.cmv.core.mediatype.validationreport.v0.ConstraintViolationV0;
import eu.cessda.cmv.core.mediatype.validationreport.v0.ValidationReportV0;
import org.gesis.commons.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

class ValidationServiceV0 implements ValidationService
{
	private static final Logger LOGGER = LoggerFactory.getLogger( ValidationServiceV0.class );

	private final CessdaMetadataValidatorFactory factory;

	ValidationServiceV0( CessdaMetadataValidatorFactory factory )
	{
		this.factory = factory;
	}

	@Override
	public ValidationReportV0 validate(
			URI documentUri,
			URI profileUri,
			ValidationGateName validationGateName )
	{
		Document document = factory.newDocument( documentUri );
		Profile profile = factory.newProfile( profileUri );
		return validate( document,
				documentUri,
				profile,
				profileUri,
				validationGateName );
	}

	@Override
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

	private static ValidationReportV0 validate( Document document, Profile profile, URI documentUri, URI profileUri, ValidationGate validationGate )
	{
		ValidationReportV0 validationReport = validate( document, documentUri, profile, validationGate );
		LOGGER.info( "Validation executed: CUSTOM, {}, {}", documentUri, profileUri );
		return validationReport;
	}

	private static ValidationReportV0 validate(
			Document document,
			URI documentUri,
			Profile profile,
			URI profileUri,
			ValidationGateName validationGateName )
	{
		ValidationReportV0 validationReport = validate( document, documentUri, profile, validationGateName.getValidationGate() );
		LOGGER.info( "Validation executed: {}, {}, {}", validationGateName, documentUri, profileUri );
		return validationReport;
	}

	private static ValidationReportV0 validate( Document document, URI documentUri, Profile profile, ValidationGate validationGate )
	{
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		ValidationReportV0 validationReport = new ValidationReportV0();
		validationReport.setDocumentUri( documentUri );
		validationReport.setConstraintViolations( constraintViolations.stream()
				.map( ConstraintViolationV0::new )
				.collect( Collectors.toList() ) );

		return validationReport;
	}

	@Override
	public ValidationReportV0 validate(
			URI documentUri,
			URI profileUri,
			ValidationGate validationGate )
	{
		Document document = factory.newDocument( documentUri );
		Profile profile = factory.newProfile( profileUri );
		return ValidationServiceV0.validate( document,
				profile,
				documentUri,
				profileUri,
				validationGate );
	}

	@Override
	public ValidationReportV0 validate(
			Resource documentResource,
			Resource profileResource,
			ValidationGate validationGate )
	{
		Document document = factory.newDocument( documentResource );
		Profile profile = factory.newProfile( profileResource );
		return ValidationServiceV0.validate( document,
				profile,
				documentResource.getUri(),
				profileResource.getUri(),
				validationGate );
	}
}
