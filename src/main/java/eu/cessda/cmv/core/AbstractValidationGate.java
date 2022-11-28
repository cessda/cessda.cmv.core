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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

abstract class AbstractValidationGate implements ValidationGate.V10
{
	private final List<Class<? extends ValidationGate.V10>> constraintTypes;

	AbstractValidationGate()
	{
		constraintTypes = new ArrayList<>();
	}

	protected void addConstraintType( String canonicalName )
	{
		requireNonNull( canonicalName );
		try
		{
			constraintTypes.add( Class.forName( canonicalName ).asSubclass( V10.class ) );
		}
		catch ( ClassNotFoundException | ClassCastException e )
		{
			throw new IllegalArgumentException( e );
		}
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends ConstraintViolation> List<T> validate( Document document, Profile profile )
	{
		requireNonNull( document );
		requireNonNull( profile );

		return (List<T>) ((Profile.V10) profile).getConstraints().stream()
				.filter( constraint -> constraintTypes.contains( constraint.getClass() ) )
				.map( Constraint.V20.class::cast )
				.map( constraint -> constraint.newValidators( document ) )
				.flatMap( List::stream )
				.map( Validator.V10.class::cast )
				.map( Validator.V10::validate )
				.filter( Optional::isPresent ).map( Optional::get )
				.collect( Collectors.toList() );
	}
}
