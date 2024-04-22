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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 *	A constraint specifying an XML node (element or attribute) described by a predicate-less
 *	XPath expression must be present and not blank if the element's parent is present.
 */
@XmlType( name = MandatoryNodeIfParentPresentConstraint.JAXB_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class MandatoryNodeIfParentPresentConstraint extends NodeConstraint
{
	public static final String JAXB_ELEMENT = "MandatoryNodeIfParentPresentConstraint";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	/**
	 * Construct a new mandatory node if parent present constraint with the location set to {@code null}.
	 */
	public MandatoryNodeIfParentPresentConstraint()
	{
		super( null );
	}

	/**
	 * Construct a new mandatory node if parent present constraint.
	 *
	 * @param locationPath the XPath of the node.
	 */
	public MandatoryNodeIfParentPresentConstraint( String locationPath )
	{
		super( locationPath );
	}
}
