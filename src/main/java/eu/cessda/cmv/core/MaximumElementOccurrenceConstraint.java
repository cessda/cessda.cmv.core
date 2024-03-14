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

import javax.xml.xpath.XPathExpressionException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class MaximumElementOccurrenceConstraint extends NodeConstraint
{
	private final long maxOccurs;

	public MaximumElementOccurrenceConstraint( String locationPath, long maxOccurs )
	{
		super( locationPath );
		this.maxOccurs = maxOccurs;
	}

	@Override
	public List<Validator> newNodeValidators( Document document ) throws XPathExpressionException
	{
		long actualCount = document.getNodes( getLocationPath() ).size();
		return Collections.singletonList( new MaximumElementOccurrenceValidator( getLocationPath(), actualCount, maxOccurs ) );
	}

	long getMaxOccurs()
	{
		return maxOccurs;
	}

	@Override
	public boolean equals( Object o )
	{
		if ( this == o ) return true;
		if ( o == null || getClass() != o.getClass() ) return false;
		if ( !super.equals( o ) ) return false;
		MaximumElementOccurrenceConstraint that = (MaximumElementOccurrenceConstraint) o;
		return maxOccurs == that.maxOccurs;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( super.hashCode(), maxOccurs );
	}
}
