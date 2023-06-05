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
package eu.cessda.cmv.core.controlledvocabulary;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class InMemoryAnalysisUnit10ControlledVocabularyRepository implements ControlledVocabularyRepository.V10
{
	private static final Set<String> CODE_VALUES = Collections.unmodifiableSet( new HashSet<>(
		Arrays.asList(
			"Individual",
			"Organization",
			"Family",
			"Family.HouseholdFamily",
			"Household",
			"HousingUnit",
			"EventOrProcess",
			"GeographicUnit",
			"TimeUnit",
			"TextUnit",
			"Group",
			"Object",
			"Other"
		)
	) );

	// The common instance for all InMemoryAnalysisUnit10ControlledVocabularyRepository objects
	private static final InMemoryAnalysisUnit10ControlledVocabularyRepository INSTANCE = new InMemoryAnalysisUnit10ControlledVocabularyRepository();

	/**
	 * Return an instance of InMemoryAnalysisUnit10ControlledVocabularyRepository.
	 */
	public static InMemoryAnalysisUnit10ControlledVocabularyRepository instance() {
		return INSTANCE;
	}

	private InMemoryAnalysisUnit10ControlledVocabularyRepository() {}

	@Override
	public Set<String> findCodeValues()
	{
		return CODE_VALUES;
	}
}
