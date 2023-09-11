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

import org.gesis.commons.xml.xpath.XPathTokenizer;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.gesis.commons.xml.xpath.XPathTokenizer.PARENT;

class MandatoryNodeIfParentPresentValidator implements Validator
{
	private final XPathTokenizer tokenizer;
	private final Node parentNode;

	public MandatoryNodeIfParentPresentValidator( XPathTokenizer tokenizer, Node parentNode )
	{
		this.tokenizer = tokenizer;
		this.parentNode = parentNode;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		if ( parentNode.getChildCount( tokenizer.getLocationPathRelativeTo( PARENT ) ) == 0 )
		{
			return of( newConstraintViolation() );
		}
		else
		{
			return empty();
		}
	}

	protected ConstraintViolation newConstraintViolation()
	{
		String message = String.format( "'%s' is mandatory in %s",
			tokenizer.getLocationPathRelativeTo( PARENT ),
			tokenizer.getLocationPath( PARENT ) );
		return new ConstraintViolation( message, parentNode.getLocationInfo() );
	}
}
