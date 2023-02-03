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

public interface ValidationGate
{
	interface V10 extends ValidationGate
	{
		<T extends ConstraintViolation> List<T> validate( Document document, Profile profile );

		/**
		 * Creates a validation gate for the given constraints. The constraints are passed as the short name
		 * of the class (i.e. FixedValueNodeConstraint)
		 *
		 * @param constraints the constraints to be added
		 * @return the validation gate
		 * @throws InvalidGateException if any of the given constraints are invalid
		 */
		static ValidationGate.V10 newValidationGate( Collection<String> constraints ) throws InvalidGateException
		{
			ArrayList<InvalidConstraintException> exceptions = new ArrayList<>();

			// Attempt to map the names of constraints to class objects
			List<Class<? extends Constraint.V20>> list = new ArrayList<>();
			for ( String c : constraints )
			{
				try
				{
					// Construct the fully qualified class name for the constraints
					Class<?> potentialConstraint = Class.forName( "eu.cessda.cmv.core." + c );
					list.add( potentialConstraint.asSubclass( Constraint.V20.class ) );
				}
				catch ( ClassNotFoundException | ClassCastException e )
				{
					exceptions.add( new InvalidConstraintException( c, e ) );
				}
			}


			if ( exceptions.isEmpty() )
			{
				// All constraints successfully added
				return new AbstractValidationGate( list )
				{
				};
			}
			else
			{
				throw new InvalidGateException( exceptions );
			}
		}
	}
}
