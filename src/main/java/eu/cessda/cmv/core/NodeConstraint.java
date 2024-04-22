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
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Base class for constraints that reference an XML using an XPath
 */
abstract class NodeConstraint implements Constraint
{
	private final String locationPath;

	/**
	 * Construct a new NodeConstraint using the given location XPath.
	 *
	 * @param locationPath the location of the node
	 */
	protected NodeConstraint( String locationPath )
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

	/**
	 * Create a new node validator, extends {@link #newValidators(Document)} to support throwing {@link XPathExpressionException}.
	 *
	 * @param document the document.
	 * @return a list of validators.
	 * @throws XPathExpressionException if there is a problem evaluating the XPath.
	 */
	protected abstract List<Validator> newNodeValidators( Document document ) throws XPathExpressionException;

	protected String getLocationPath()
	{
		return locationPath;
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
