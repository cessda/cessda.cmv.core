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

import eu.cessda.cmv.core.mediatype.validationreport.ValidationReport;

import java.util.ArrayList;
import java.util.List;

class ValidationServiceImpl implements ValidationService
{
	public ValidationReport validate( Document document, Profile profile, ValidationGateName validationGateName )
	{
		return validate( document, profile, validationGateName.getValidationGate() );
	}

	public ValidationReport validate( Document document, Profile profile, ValidationGate validationGate )
	{
		List<eu.cessda.cmv.core.ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// Transform all constraint violations into the media-type form
		List<eu.cessda.cmv.core.mediatype.validationreport.ConstraintViolation> mediaTypeConstraintViolations = new ArrayList<>( constraintViolations.size() );
		for ( eu.cessda.cmv.core.ConstraintViolation constraintViolation : constraintViolations )
		{
			eu.cessda.cmv.core.mediatype.validationreport.ConstraintViolation mediaTypeViolation = new eu.cessda.cmv.core.mediatype.validationreport.ConstraintViolation( constraintViolation );
			mediaTypeConstraintViolations.add( mediaTypeViolation );
		}

		// Create the validation report
		return new ValidationReport( document.getURI(), mediaTypeConstraintViolations );
	}
}
