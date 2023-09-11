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

import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;

class ControlledVocabularyRepositoryValidator implements Validator
{
	private final ControlledVocabularyRepository repository;

	public ControlledVocabularyRepositoryValidator( ControlledVocabularyRepository repository )
	{
		requireNonNull( repository );
		this.repository = repository;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		Set<String> codeValues;

		try
		{
			codeValues = repository.findCodeValues();
		}
		catch (IllegalStateException e)
		{
			return Optional.of( new ConstraintViolation( e.getMessage(), null ) );
		}

		if ( codeValues.isEmpty() )
		{
			// No code values found, fail the constraint
			return Optional.of( new ConstraintViolation( "No values", null ) );
		}
		else
		{
			return Optional.empty();
		}
	}
}
