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

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepositoryProxy;

import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;

class ControlledVocabularyRepositoryValidator implements Validator.V10
{
	private final ControlledVocabularyRepositoryProxy proxy;

	public ControlledVocabularyRepositoryValidator( ControlledVocabularyRepositoryProxy proxy )
	{
		requireNonNull( proxy );
		this.proxy = proxy;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		try
		{
			proxy.unproxy();
		}
		catch (Exception e)
		{
			return Optional.of( new ConstraintViolation( e.getMessage(), empty() ) );
		}
		if ( proxy.findCodeValues().isEmpty() )
		{
			return Optional.of( new ConstraintViolation( "No values", empty() ) );
		}
		return empty();
	}
}
