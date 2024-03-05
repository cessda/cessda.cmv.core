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
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

class FixedValueNodeConstraint extends NodeConstraint
{
	private final String fixedValue;

	public FixedValueNodeConstraint( String locationPath, String fixedValue )
	{
		super( locationPath );
		this.fixedValue = requireNonNull( fixedValue );
	}

	@Override
	public List<Validator> newNodeValidators( Document document ) throws XPathExpressionException
	{
		List<Node> nodes = document.getNodes( getLocationPath() );
		List<Validator> validators = new ArrayList<>();
		for ( Node node : nodes )
		{
			FixedValueNodeValidator fixedValueNodeValidator = new FixedValueNodeValidator( node, fixedValue );
			validators.add( fixedValueNodeValidator );
		}
		return validators;
	}
}
