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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;

/**
 * 	A constraint that enforces the metadata document is valid only if the
 * 	node value equals to the fixed value defined in the constraint.
 */
@XmlType( name = FixedValueNodeConstraint.JAXB_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class FixedValueNodeConstraint extends NodeConstraint
{
	public static final String JAXB_ELEMENT = "FixedValueNodeConstraint";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	@XmlElement
	private String fixedValue;

	/**
	 * Construct a new fixed value node constraint with the location and fixed value set to {@code null}.
	 */
	public FixedValueNodeConstraint()
	{
		super( null );
		this.fixedValue = null;
	}

	/**
	 * Construct a new fixed value node constraint.
	 *
	 * @param locationPath the XPath of the node.
	 * @param fixedValue the fixed value of the node.
	 */
	public FixedValueNodeConstraint( String locationPath, String fixedValue )
	{
		super( locationPath );
		this.fixedValue = fixedValue;
	}

	public String getFixedValue()
	{
		return fixedValue;
	}

	public void setFixedValue( String fixedValue )
	{
		this.fixedValue = fixedValue;
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
