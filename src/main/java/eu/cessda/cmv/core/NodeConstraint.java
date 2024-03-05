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
import java.util.List;

import static java.util.Objects.requireNonNull;

abstract class NodeConstraint implements Constraint
{
	private final String locationPath;

	NodeConstraint( String locationPath )
	{
		requireNonNull( locationPath );
		// TODO: Validate XPath
		this.locationPath = locationPath;
	}

	@Override
	public List<Validator> newValidators( Document document )
	{
		try
		{
			return newNodeValidators(document);
		}
		catch ( XPathExpressionException e )
		{
			// XPaths from getLocationPath() should be validated before this method is called
			throw new IllegalStateException( e );
		}
	}

	protected abstract List<Validator> newNodeValidators( Document document) throws XPathExpressionException;

	protected String getLocationPath()
	{
		return locationPath;
	}
}
