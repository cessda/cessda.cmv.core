/*-
 * #%L
 * cmv-core
 * %%
 * Copyright (C) 2020 - 2024 CESSDA ERIC
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

import java.util.Objects;

/**
 * This class implements the {@link Object#equals(Object)} and {@link Object#hashCode()} methods
 * to fulfill the equality contract between instances of {@link Profile}.
 * <p>
 * Profiles are considered to be equal if each profile has the same constraints. Performing a validation using
 * equivalent profiles should emit identical constraint violations. The ordering of the constraints is unimportant.
 * <p>
 * The profile name and version is not considered important for the purposes of profile equality, and is ignored.
 */
public abstract class AbstractProfile implements Profile
{
	/**
	 * Tests two profiles for equivalence. Profiles are equivalent if they have identical constraints.
	 *
	 * @param obj the reference object to compare.
	 * @return {@code true} if the profiles have the same constraints, {@code false} otherwise.
	 */
	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj ) return true;
		if ( !( obj instanceof AbstractProfile ) ) return false;
		Profile that = (Profile) obj;
		return Objects.equals( getConstraints(), that.getConstraints() );
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( getConstraints() );
	}
}
