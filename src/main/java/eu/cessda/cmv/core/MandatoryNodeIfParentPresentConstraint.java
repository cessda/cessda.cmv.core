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

import org.gesis.commons.xml.xpath.XPathTokenizer;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

import static org.gesis.commons.xml.xpath.XPathTokenizer.PARENT;

/**
 *	A constraint specifying an XML node (element or attribute) described by a predicate-less
 *	XPath expression must be present and not blank if the element's parent is present.
 * 	<p>
 * 	This constraint can be defined in a DDI profile using this representation
 * 	<pre>{@code
 * 	<pr:Used xpath="/codeBook/stdyDscr/citation/titlStmt/IDNo/@agency" isRequired="false">
 *		<pr:Instructions>
 *			<r:Content>
 *		 	<!-- Constraint is a string, it must be escaped or wrapped in a CDATA section -->
 *				<![CDATA[
 *				<Constraints>
 *					<MandatoryNodeIfParentPresentConstraint/>
 * 				</Constraints>
 *				]]>
 *			</r:Content>
 *		</pr:Instructions>
 *	</pr:Used>}
 */
class MandatoryNodeIfParentPresentConstraint extends NodeConstraint
{
	/**
	 * Construct a new mandatory node constraint.
	 *
	 * @param locationPath the XPath of the node.
	 */
	public MandatoryNodeIfParentPresentConstraint( String locationPath )
	{
		super( locationPath );
	}

	@Override
	public List<Validator> newNodeValidators( Document document ) throws XPathExpressionException
	{
		List<Validator> validators = new ArrayList<>();

		List<Node> nodes = document.getNodes( getLocationPath() );
		for ( Node node : nodes )
		{
			validators.add( new NotBlankNodeValidator( node ) );
		}

		XPathTokenizer tokenizer = new XPathTokenizer( getLocationPath() );
		List<Node> parentNodes = document.getNodes( tokenizer.getLocationPath( PARENT ) );
		for ( Node node : parentNodes )
		{
			validators.add( new MandatoryNodeIfParentPresentValidator( tokenizer, node ) );
		}

		return validators;
	}
}
