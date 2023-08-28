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

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class Node
{
	private final String locationPath;
	private final String textContent;
	private final LocationInfo locationInfo;
	private final Map<String, Integer> childCounter = new HashMap<>();

	Node( String locationPath, String textContent, LocationInfo locationInfo )
	{
		requireNonNull( locationPath );

		this.locationPath = locationPath;
		this.textContent = textContent;
		this.locationInfo = locationInfo;
	}

	public String getLocationPath()
	{
		return locationPath;
	}

	public String getTextContent()
	{
		return textContent;
	}

	public LocationInfo getLocationInfo()
	{
		return locationInfo;
	}

	void incrementChildCount( String relativeLocationPath )
	{
		requireNonNull( relativeLocationPath );
		childCounter.compute( relativeLocationPath, (path, counter) ->
		{
			if ( counter == null )
			{
				// Initialise the mapping with 1
				return 1;
			}
			else
			{
				return counter + 1;
			}
		} );
	}

	int getChildCount( String relativeLocationPath )
	{
		return childCounter.getOrDefault( relativeLocationPath, 0 );
	}
}
