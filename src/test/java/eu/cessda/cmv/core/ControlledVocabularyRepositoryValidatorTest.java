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

import eu.cessda.cmv.core.controlledvocabulary.CessdaControlledVocabularyRepositoryV2;
import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepositoryProxy;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.gesis.commons.test.hamcrest.OptionalMatchers.isEmpty;
import static org.gesis.commons.test.hamcrest.OptionalMatchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

class ControlledVocabularyRepositoryValidatorTest
{
	@Test
	void validate_valid()
	{
		// given
		String canonicalName = CessdaControlledVocabularyRepositoryV2.class.getCanonicalName();
		String uri = "https://vocabularies.cessda.eu/v2/vocabularies/AnalysisUnit/2.0?languageVersion=en-2.0";
		ControlledVocabularyRepositoryProxy proxy = new ControlledVocabularyRepositoryProxy( canonicalName, uri );

		// when
		Validator validator = new ControlledVocabularyRepositoryValidator( proxy );
		Optional<ConstraintViolation> constraintViolation = validator.validate();

		// then
		assertThat( constraintViolation, isEmpty() );
	}

	@Test
	void validate_invalid()
	{
		// given
		String canonicalName = CessdaControlledVocabularyRepositoryV2.class.getCanonicalName();
		String uri = "https://localhost/v2/vocabularies/AnalysisUnit/10.0?languageVersion=en-10.0";
		ControlledVocabularyRepositoryProxy proxy = new ControlledVocabularyRepositoryProxy( canonicalName, uri );

		// when
		Validator validator = new ControlledVocabularyRepositoryValidator( proxy );
		Optional<ConstraintViolation> constraintViolation = validator.validate();

		// then
		assertThat( constraintViolation, isPresent() );
		assertThat( constraintViolation.get().getMessage(), containsString( "Resource not found" ) );
	}
}
