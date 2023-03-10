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

import java.util.HashSet;
import java.util.Set;

public class InMemoryAnalysisUnit10ControlledVocabularyRepository implements ControlledVocabularyRepository.V10
{
	@Override
	public Set<String> findCodeValues()
	{
		Set<String> codeValues = new HashSet<>();
		codeValues.add( "Individual" );
		codeValues.add( "Organization" );
		codeValues.add( "Family" );
		codeValues.add( "Family.HouseholdFamily" );
		codeValues.add( "Household" );
		codeValues.add( "HousingUnit" );
		codeValues.add( "EventOrProcess" );
		codeValues.add( "GeographicUnit" );
		codeValues.add( "TimeUnit" );
		codeValues.add( "TextUnit" );
		codeValues.add( "Group" );
		codeValues.add( "Object" );
		codeValues.add( "Other" );
		return codeValues;
	}
}
