/*-
 * #%L
 * cmv-core
 * %%
 * Copyright (C) 2020 - 2023 CESSDA ERIC
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
package eu.cessda.cmv.core.controlledvocabulary;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class EmptyControlledVocabularyRepositoryTest
{

	@Test
	void shouldReturnEmptyCodeValues()
	{
		EmptyControlledVocabularyRepository repository = EmptyControlledVocabularyRepository.INSTANCE;

		// Code values should be empty
		assertThat( repository.findCodeValues(), is( empty() ) );
	}

	@Test
	void shouldReturnEmptyDescriptiveTerms()
	{
		EmptyControlledVocabularyRepository repository = EmptyControlledVocabularyRepository.INSTANCE;

		// Code values should be empty
		assertThat( repository.findDescriptiveTerms(), is( empty() ) );
	}

	@Test
	void shouldReturnAValidRandomUUID()
	{
		EmptyControlledVocabularyRepository repository = EmptyControlledVocabularyRepository.INSTANCE;

		// Retrive the UUID
		URI uri = repository.getUri();
		String uuidString = uri.getSchemeSpecificPart().split( ":" )[1];

		// Check if the UUID is valid
		assertDoesNotThrow( () -> UUID.fromString( uuidString ) );
	}

	@Test
	void shouldAlwaysBeEqual()
	{
		EmptyControlledVocabularyRepository firstRepository = EmptyControlledVocabularyRepository.INSTANCE;
		EmptyControlledVocabularyRepository secondRepository = EmptyControlledVocabularyRepository.INSTANCE;

		// The objects should be considered equal, and the hash codes should match
		assertThat( firstRepository, equalTo( secondRepository ) );
		assertThat( firstRepository.hashCode(), equalTo( secondRepository.hashCode() ) );
	}
}
