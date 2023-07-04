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

class MinimumElementOccurrenceValidator implements Validator
{
	private final String locationPath;
	private final long actualOccurs;
	private final long minOccurs;

	MinimumElementOccurrenceValidator( String locationPath, long actualCount, long minOccurs )
	{
		requireNonNull( locationPath );
		requireNonNegativeLong( actualCount, "actualCount" );
		requireNonNegativeLong( minOccurs, "minOccurs" );

		this.locationPath = locationPath;
		this.actualOccurs = actualCount;
		this.minOccurs = minOccurs;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		if ( actualOccurs < minOccurs )
		{
			return of( newConstraintViolation() );
		}
		else
		{
			return empty();
		}
	}

	private static void requireNonNegativeLong( long value, String parameter )
	{
		if ( value < 0 )
		{
			throw new IllegalArgumentException( parameter + " is negative" );
		}
	}

	protected String getLocationPath()
	{
		return locationPath;
	}

	protected ConstraintViolation newConstraintViolation()
	{
		String message = String.format( "'%s' occurs less than minimal count of %s", locationPath, minOccurs );
		return new ConstraintViolation( message, null );
	}
}
