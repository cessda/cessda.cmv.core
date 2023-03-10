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

import eu.cessda.cmv.core.mediatype.validationreport.v0.ValidationReportV0;
import org.gesis.commons.resource.Resource;

import java.net.URI;

public interface ValidationService
{
	interface V10 extends ValidationService
	{
		<T extends ValidationReport> T validate(
				URI documentUri,
				URI profileUri,
				ValidationGateName validationGateName );

		<T extends ValidationReport> T validate(
				Resource document,
				Resource profile,
				ValidationGateName validationGateName );

		ValidationReportV0 validate(
				URI documentUri,
				URI profileUri,
				ValidationGate.V10 validationGate );

		ValidationReportV0 validate(
				Resource documentResource,
				Resource profileResource,
				ValidationGate.V10 validationGate );
	}
}
