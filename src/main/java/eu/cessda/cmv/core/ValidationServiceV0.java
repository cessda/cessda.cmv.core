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

import java.net.URI;
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
		validationReport.setDocumentUri( documentUri );
		validationReport.setConstraintViolations( constraintViolations.stream()
				.map( ConstraintViolationV0::new )
				.collect( Collectors.toList() ) );
		LOGGER.info( "Validation executed: {}, {}, {}", validationGateName, documentUri, profileUri );
		return validationReport;
	}
}
