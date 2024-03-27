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
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

import static eu.cessda.cmv.core.ValidationGateName.BASIC;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

class JdkValidationServiceTest
{
	private final CessdaMetadataValidatorFactory factory = new CessdaMetadataValidatorFactory();

	@Test
	void validate() throws IOException, NotDocumentException
	{
		URL documentUri = this.getClass().getResource( "/demo-documents/ddi-v25/gesis-2800.xml" );
		URL profileUri = this.getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" );

		Document document = factory.newDocument( documentUri );
		Profile profile = factory.newProfile( profileUri );

		ValidationReport validationReportFromGateName = factory.validate( document, profile, BASIC );
		ValidationReport validationReportFromGate = factory.validate( document, profile, BASIC.getValidationGate() );

		// Assert that 4 constraint violations were found
		assertThat( validationReportFromGateName.getConstraintViolations(), hasSize( 4 ) );
		assertThat( validationReportFromGate.getConstraintViolations(), hasSize( 4 ) );

		// Assert that both reports are equal
		assertThat( validationReportFromGateName, equalTo( validationReportFromGate ) );
	}
}
