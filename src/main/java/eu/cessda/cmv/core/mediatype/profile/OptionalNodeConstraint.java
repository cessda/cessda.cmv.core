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

@XmlType( name = OptionalNodeConstraint.JAXB_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class OptionalNodeConstraint extends NodeConstraint
{
	public static final String JAXB_ELEMENT = "OptionalNodeConstraint";
	public static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	public OptionalNodeConstraint()
	{
		super( null );
	}

	public OptionalNodeConstraint( String locationPath )
	{
		super( locationPath );
	}
}
