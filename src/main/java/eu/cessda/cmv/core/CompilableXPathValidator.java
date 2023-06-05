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
import javax.xml.xpath.XPathFactory;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

class CompilableXPathValidator implements Validator.V10
{
	private final XPathFactory factory;
	private String reason;
	private final Node node;

	CompilableXPathValidator( Node node )
	{
		this( node, XPathFactory.newInstance() );
	}

	CompilableXPathValidator( Node node, XPathFactory factory )
	{
		requireNonNull( node );
		requireNonNull( factory );

		this.node = node;
		this.factory = factory;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{

		try
		{
			// XPathFactory instances are not thread safe
			synchronized ( factory )
			{
				factory.newXPath().compile( node.getTextContent() );
			}
			return empty();
		}
		catch ( XPathExpressionException e )
		{
			reason = e.getMessage().replace( "javax.xml.transform.TransformerException: ", "" );
			return of( newConstraintViolation() );
		}
	}

	private ConstraintViolation newConstraintViolation()
	{
		String message = "'%s' is not a compilable XPath: %s";
		message = String.format( message, node.getTextContent(), reason );
		return new ConstraintViolation( message, node.getLocationInfo() );
	}
}
