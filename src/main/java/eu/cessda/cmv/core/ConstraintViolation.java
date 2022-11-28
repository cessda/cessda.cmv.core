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

import org.gesis.commons.xml.LocationInfo;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class ConstraintViolation
{
	private final String message;

	private final Optional<LocationInfo> locationInfo;

	ConstraintViolation( String message, Optional<LocationInfo> locationInfo )
	{
		requireNonNull( message );
		requireNonNull( locationInfo );

		this.message = message;
		this.locationInfo = locationInfo;
	}

	public String getMessage()
	{
		if ( locationInfo.isPresent() )
		{
			return message + " (lineNumber: " + locationInfo.get().getLineNumber() + ")";
		}
		else
		{
			return message;
		}
	}

	public Optional<LocationInfo> getLocationInfo()
	{
		return locationInfo;
	}
}
