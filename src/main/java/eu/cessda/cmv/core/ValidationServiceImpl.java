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

import eu.cessda.cmv.core.mediatype.validationreport.ConstraintViolation;
import eu.cessda.cmv.core.mediatype.validationreport.ValidationReport;
import org.gesis.commons.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

class ValidationServiceImpl implements ValidationService
{
	private static final Logger LOGGER = LoggerFactory.getLogger( ValidationServiceImpl.class );

	private final CessdaMetadataValidatorFactory factory;

	ValidationServiceImpl( CessdaMetadataValidatorFactory factory )
	{
		this.factory = factory;
	}

	@Override
	public ValidationReport validate(
			URI documentUri,
			URI profileUri,
			ValidationGateName validationGateName ) throws IOException, NotDocumentException
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
	public ValidationReport validate(
			Resource documentResource,
			Resource profileResource,
			ValidationGateName validationGateName ) throws IOException, NotDocumentException
	{
		Document document = factory.newDocument( documentResource );
		Profile profile = factory.newProfile( profileResource );
		return validate( document,
				documentResource.getUri(),
				profile,
				profileResource.getUri(),
				validationGateName );
	}

	private static ValidationReport validate( Document document, Profile profile, URI documentUri, URI profileUri, ValidationGate validationGate )
	{
		ValidationReport validationReport = validate( document, documentUri, profile, validationGate );
		LOGGER.info( "Validation executed: CUSTOM, {}, {}", documentUri, profileUri );
		return validationReport;
	}

	private static ValidationReport validate(
			Document document,
			URI documentUri,
			Profile profile,
			URI profileUri,
			ValidationGateName validationGateName )
	{
		ValidationReport validationReport = validate( document, documentUri, profile, validationGateName.getValidationGate() );
		LOGGER.info( "Validation executed: {}, {}, {}", validationGateName, documentUri, profileUri );
		return validationReport;
	}

	private static ValidationReport validate( Document document, URI documentUri, Profile profile, ValidationGate validationGate )
	{
		List<eu.cessda.cmv.core.ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// Transform all constraint violations into the media-type form
		List<ConstraintViolation> mediaTypeConstraintViolations = new ArrayList<>( constraintViolations.size() );
		for ( eu.cessda.cmv.core.ConstraintViolation constraintViolation : constraintViolations )
		{
			ConstraintViolation mediaTypeViolation = new ConstraintViolation( constraintViolation );
			mediaTypeConstraintViolations.add( mediaTypeViolation );
		}

		// Create the validation report
		return new ValidationReport( documentUri, mediaTypeConstraintViolations );
	}

	@Override
	public ValidationReport validate(
			URI documentUri,
			URI profileUri,
			ValidationGate validationGate ) throws IOException, NotDocumentException
	{
		Document document = factory.newDocument( documentUri );
		Profile profile = factory.newProfile( profileUri );
		return ValidationServiceImpl.validate( document,
				profile,
				documentUri,
				profileUri,
				validationGate );
	}

	@Override
	public ValidationReport validate(
			Resource documentResource,
			Resource profileResource,
			ValidationGate validationGate ) throws IOException, NotDocumentException
	{
		Document document = factory.newDocument( documentResource );
		Profile profile = factory.newProfile( profileResource );
		return ValidationServiceImpl.validate( document,
				profile,
				documentResource.getUri(),
				profileResource.getUri(),
				validationGate );
	}
}
