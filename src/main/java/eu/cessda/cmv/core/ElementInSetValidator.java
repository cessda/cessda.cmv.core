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
import java.util.Set;

import static java.util.Optional.empty;
import static java.util.Optional.of;

class ElementInSetValidator implements Validator
{
	private final Node node;
	private final Set<String> elementSet;

	ElementInSetValidator( Node node, Set<String> elementSet )
	{
		this.node = node;
		this.elementSet = elementSet;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		if ( !elementSet.contains( node.getTextContent() ) )
		{
			return of( newConstraintViolation() );
		}
		else
		{
			return empty();
		}
	}

	private ConstraintViolation newConstraintViolation()
	{
		String message = "'%s' is not element in '%s'";
		message = String.format( message, node.getTextContent(), elementSet );
		return new ConstraintViolation( message, node.getLocationInfo() );
	}
}
