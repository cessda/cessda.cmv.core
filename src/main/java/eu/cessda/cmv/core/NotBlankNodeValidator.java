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

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

class NotBlankNodeValidator implements Validator
{

	private final Node node;

	public NotBlankNodeValidator( Node node )
	{
		this.node = node;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		boolean isBlank = node.getTextContent().trim().isEmpty();
		if ( isBlank )
		{
			return ofNullable( newConstraintViolation() );
		}
		else
		{
			return empty();
		}
	}

	protected ConstraintViolation newConstraintViolation()
	{
		String message = String.format( "'%s' is blank", node.getLocationPath() );
		return new ConstraintViolation( message, node.getLocationInfo() );
	}
}
