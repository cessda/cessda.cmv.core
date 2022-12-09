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

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepository;
import org.gesis.commons.xml.LocationInfo;

class ControlledVocabularyNode extends Node
{
	private final ControlledVocabularyRepository.V11 controlledVocabularyRepository;

	ControlledVocabularyNode( String locationPath,
							  String textContent,
							  LocationInfo locationInfo,
							  ControlledVocabularyRepository.V11 controlledVocabularyRepository )
	{
		super( locationPath, textContent, locationInfo );
		this.controlledVocabularyRepository = controlledVocabularyRepository;
	}

	ControlledVocabularyRepository.V11 getControlledVocabularyRepository()
	{
		return controlledVocabularyRepository;
	}
}
