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

import static eu.cessda.cmv.core.ValidationGateName.BASIC;
import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;

import org.gesis.commons.resource.Resource;
import org.junit.jupiter.api.Test;

import eu.cessda.cmv.core.mediatype.validationreport.v0.ValidationReportV0;

class JdkValidationServiceTest
{
	private CessdaMetadataValidatorFactory factory;

	JdkValidationServiceTest()
	{
		factory = new CessdaMetadataValidatorFactory();
	}

	@Test
	void validateWithUrls() throws MalformedURLException
	{
		ValidationService.V10 validationService = factory.newValidationService();
		URI documentUri = new File( "src/main/resources/demo-documents/ddi-v25/gesis-2800.xml" ).toURI();
		URI profileUri = new File( "src/main/resources/demo-documents/ddi-v25/cdc25_profile.xml" ).toURI();
		ValidationReportV0 validationReport = validationService.validate( documentUri, profileUri, BASIC );
		assertThat( validationReport.getConstraintViolations(), hasSize( 4 ) );
	}

	@Test
	void validateWithResources()
	{
		ValidationService.V10 validationService = factory.newValidationService();
		Resource document = newResource( new File( "src/main/resources/demo-documents/ddi-v25/gesis-2800.xml" ) );
		Resource profile = newResource( new File( "src/main/resources/demo-documents/ddi-v25/cdc25_profile.xml" ) );
		ValidationReportV0 validationReport = validationService.validate( document, profile, BASIC );
		assertThat( validationReport.getConstraintViolations(), hasSize( 4 ) );
	}
}
