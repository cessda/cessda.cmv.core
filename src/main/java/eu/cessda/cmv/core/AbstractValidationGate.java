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
import java.util.List;

import static java.util.Objects.requireNonNull;

abstract class AbstractValidationGate implements ValidationGate.V10
{
	private final List<Class<? extends Constraint.V20>> constraintTypes;

	AbstractValidationGate()
	{
		constraintTypes = new ArrayList<>();
	}

	protected void addConstraintType( Class<? extends Constraint.V20> constraint )
	{
		constraintTypes.add( constraint );
	}

	protected void addConstraintType( Collection<Class<? extends Constraint.V20>> constraints )
	{
		constraintTypes.addAll( constraints );
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends ConstraintViolation> List<T> validate( Document document, Profile profile )
	{
		requireNonNull( document );
		requireNonNull( profile );

		List<ConstraintViolation> constraintViolations = new ArrayList<>();
		for ( Constraint constraint : ( (Profile.V10) profile ).getConstraints() )
		{
			if ( constraintTypes.contains( constraint.getClass() ) )
			{
				for ( Validator validator : ( (Constraint.V20) constraint ).newValidators( document ) )
				{
					( (Validator.V10) validator ).validate().ifPresent( constraintViolations::add );
				}
			}
		}
		return (List<T>) constraintViolations;
	}
}
