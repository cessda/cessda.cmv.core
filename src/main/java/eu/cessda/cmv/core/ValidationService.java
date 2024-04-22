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

import java.io.IOException;
import java.net.URI;

public interface ValidationService
{
	/**
	 * Validates the document against the given profile using the specified validation gate.
	 *
	 * @param documentUri the URI of the document to validate.
	 * @param profileUri the URI of the profile to validate the document against.
	 * @param validationGateName the validation gate to use.
	 * @return the validation report.
	 * @deprecated use {@link ValidationService#validate(Document, Profile, ValidationGate)} instead.
	 */
	@Deprecated
	default ValidationReport validate(
			URI documentUri,
			URI profileUri,
			ValidationGateName validationGateName ) throws IOException, NotDocumentException
	{
		return validate( documentUri, profileUri, validationGateName.getValidationGate() );
	}

	/**
	 * Validates the document against the given profile using the specified validation gate.
	 *
	 * @param document the document to validate.
	 * @param profile the profile to validate the document against.
	 * @param validationGateName the validation gate to use.
	 * @return the validation report.
	 */
	default ValidationReport validate(
			Document document,
			Profile profile,
			ValidationGateName validationGateName ) throws IOException, NotDocumentException
	{
		return validate( document, profile, validationGateName.getValidationGate() );
	}

	/**
	 * Validates the document against the given profile using the specified validation gate.
	 *
	 * @param documentUri the URI of the document to validate.
	 * @param profileUri the URI of the profile to validate the document against.
	 * @param validationGate the validation gate to use.
	 * @return the validation report.
	 * @deprecated use {@link ValidationService#validate(Document, Profile, ValidationGate)} instead.
	 */
	@Deprecated
	ValidationReport validate(
			URI documentUri,
			URI profileUri,
			ValidationGate validationGate ) throws IOException, NotDocumentException;

	/**
	 * Validates the document against the given profile using the specified validation gate.
	 *
	 * @param document the document to validate.
	 * @param profile the profile to validate the document against.
	 * @param validationGate the validation gate to use.
	 * @return the validation report.
	 */
	ValidationReport validate(
			Document document,
			Profile profile,
			ValidationGate validationGate ) throws IOException, NotDocumentException;
}
