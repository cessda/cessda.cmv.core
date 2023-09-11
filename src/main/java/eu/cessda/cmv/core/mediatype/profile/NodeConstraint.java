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
package eu.cessda.cmv.core.mediatype.profile;

import javax.xml.bind.annotation.XmlElement;
import java.util.Objects;

public abstract class NodeConstraint extends Constraint
{
	@XmlElement
	protected String locationPath;

	protected NodeConstraint( String locationPath )
	{
		this.locationPath = locationPath;
	}

	public String getLocationPath()
	{
		return locationPath;
	}

	public void setLocationPath( String locationPath )
	{
		this.locationPath = locationPath;
	}

	@Override
	public boolean equals( Object o )
	{
		if ( this == o ) return true;
		if ( o == null || getClass() != o.getClass() ) return false;
		NodeConstraint that = (NodeConstraint) o;
		return Objects.equals( locationPath, that.locationPath );
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( locationPath );
	}
}
