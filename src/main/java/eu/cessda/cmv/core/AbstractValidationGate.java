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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static java.util.Objects.requireNonNull;

abstract class AbstractValidationGate implements ValidationGate
{
	private final HashSet<Class<? extends Constraint>> constraintTypes;

	AbstractValidationGate()
	{
		constraintTypes = new HashSet<>();
	}

	AbstractValidationGate( Collection<Class<? extends Constraint>> constraints )
	{
		this.constraintTypes = new HashSet<>( constraints );
	}

	protected void addConstraintType( Collection<Class<? extends Constraint>> constraints )
	{
		constraintTypes.addAll( constraints );
	}

	@Override
	public List<ConstraintViolation> validate( Document document, Profile profile )
	{
		requireNonNull( document, "document is null" );
		requireNonNull( profile, "profile is null" );

		// Set the namespace context
		document.setNamespaceContext( profile.getNamespaceContext() );

		// Validate the document against the constraints
		List<ConstraintViolation> constraintViolations = new ArrayList<>();
		for ( Constraint constraint : profile.getConstraints() )
		{
			if ( constraintTypes.contains( constraint.getClass() ) )
			{
				for ( Validator validator : constraint.newValidators( document ) )
				{
					validator.validate().ifPresent( constraintViolations::add );
				}
			}
		}
		return constraintViolations;
	}
}
