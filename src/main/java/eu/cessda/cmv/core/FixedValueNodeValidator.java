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

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

class FixedValueNodeValidator implements Validator.V10
{
	private final Node node;
	private final String fixedValue;

	public FixedValueNodeValidator( Node node, String fixedValue )
	{
		this.node = requireNonNull( node );
		this.fixedValue = requireNonNull( fixedValue );
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		if ( fixedValue.equals( node.getTextContent().trim() ) )
		{
			return empty();
		}
		else
		{
			return of( newConstraintViolation() );
		}
	}

	protected ConstraintViolation newConstraintViolation()
	{
		String message = "'%s' is not equal to fixed value '%s' in '%s'";
		message = String.format( message, node.getTextContent(), fixedValue, node.getLocationPath() );
		return new ConstraintViolation( message, node.getLocationInfo() );
	}
}
