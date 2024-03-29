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

import java.util.Collections;
import java.util.List;

class MaximumElementOccurrenceConstraint extends NodeConstraint
{
	private final long maxOccurs;

	public MaximumElementOccurrenceConstraint( String locationPath, long maxOccurs )
	{
		super( locationPath );
		this.maxOccurs = maxOccurs;
	}

	@Override
	public List<Validator> newValidators( Document document )
	{
		long actualCount = document.getNodes( getLocationPath() ).size();
		return Collections.singletonList( new MaximumElementOccurrenceValidator( getLocationPath(), actualCount, maxOccurs ) );
	}
}
