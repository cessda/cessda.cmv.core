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

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.gesis.commons.xml.LocationInfo;

class Node
{
	private String locationPath;
	private String textContent;
	private Optional<LocationInfo> locationInfo;
	private Map<String, Integer> childCounter;

	Node( String locationPath, String textContent, Optional<LocationInfo> locationInfo )
	{
		requireNonNull( locationPath );
		requireNonNull( locationInfo );

		this.locationPath = locationPath;
		this.textContent = textContent;
		this.locationInfo = locationInfo;
		childCounter = new HashMap<>();
	}

	public String getLocationPath()
	{
		return locationPath;
	}

	public String getTextContent()
	{
		return textContent;
	}

	public Optional<LocationInfo> getLocationInfo()
	{
		return locationInfo;
	}

	public void incrementChildCount( String relativeLocationPath )
	{
		requireNonNull( relativeLocationPath );
		if ( !childCounter.containsKey( relativeLocationPath ) )
		{
			childCounter.put( relativeLocationPath, 0 );
		}
		childCounter.put( relativeLocationPath, childCounter.get( relativeLocationPath ) + 1 );
	}

	public int getChildCount( String relativeLocationPath )
	{
		if ( childCounter.containsKey( relativeLocationPath ) )
		{
			return childCounter.get( relativeLocationPath );
		}
		else
		{
			return 0;
		}
	}
}
