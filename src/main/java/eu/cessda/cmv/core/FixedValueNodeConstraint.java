/*-
 * #%L
 * cmv-core
 * %%
 * Copyright (C) 2020 - 2024 CESSDA ERIC
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
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * 	A constraint that enforces the metadata document is valid only if the
 * 	node value equals to the fixed value defined in the constraint.
 * 	<p>
 * 	This constraint can be defined in a DDI profile using this representation
 * 	<pre>{@code
 *  <pr:Used
 * 		xpath="/codeBook/stdyDscr/stdyInfo/sumDscr/anlyUnit/concept/@vocab"
 *  	defaultValue="DDI Analysis Unit"
 *   	fixedValue="true"/>
 *  </pr:Used>}
 * </pre>
 */
class FixedValueNodeConstraint extends NodeConstraint
{
	private final String fixedValue;

	/**
	 * Construct a new fixed value node constraint.
	 *
	 * @param locationPath the XPath of the node.
	 * @param fixedValue the fixed value of the node.
	 */
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

	public String getFixedValue()
	{
		return fixedValue;
	}

	@Override
	public String toString()
	{
		return "FixedValueNodeConstraint{" +
			"locationPath='" + getLocationPath() + '\'' +
			", fixedValue='" + fixedValue + '\'' +
			'}';
	}

	@Override
	public boolean equals( Object o )
	{
		if ( this == o ) return true;
		if ( o == null || getClass() != o.getClass() ) return false;
		if ( !super.equals( o ) ) return false;
		FixedValueNodeConstraint that = (FixedValueNodeConstraint) o;
		return Objects.equals( fixedValue, that.fixedValue );
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( super.hashCode(), fixedValue );
	}
}
