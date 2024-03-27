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

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepository;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;

abstract class ElementOfControlledVocabularyValidator implements Validator
{
	private final ControlledVocabularyNode node;
	private final ElementName elementName;

	ElementOfControlledVocabularyValidator( ControlledVocabularyNode node, ElementName elementName )
	{
		requireNonNull( node );
		requireNonNull( elementName );

		this.node = node;
		this.elementName = elementName;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		ControlledVocabularyRepository repository = node.getControlledVocabularyRepository();
		if ( repository == null )
		{
			String message = format( "%s '%s' in '%s' cannot be validated because no controlled vocabulary is declared",
					elementName.getText(), node.getTextContent(), node.getLocationPath() );
			return of( new ConstraintViolation( message, node.getLocationInfo() ) );
		}
		else
		{
			Set<String> elementSet = elementName.getElementSet( repository );
			if ( !elementSet.contains( node.getTextContent() ) )
			{
				String message = format( "%s '%s' in '%s' is not element of the controlled vocabulary in '%s'",
						elementName.getText(), node.getTextContent(), node.getLocationPath(), repository.getUri() );
				return Optional.of( new ConstraintViolation( message, node.getLocationInfo() ) );
			}
			else
			{
				return Optional.empty();
			}
		}
	}

	enum ElementName
	{
		CODE_VALUE( "Code value", ControlledVocabularyRepository::findCodeValues ),
		DESCRIPTIVE_TERM( "Descriptive term", ControlledVocabularyRepository::findDescriptiveTerms );

		private final String text;
		private final Function<ControlledVocabularyRepository, Set<String>> elementSetProvider;

		ElementName( String text, Function<ControlledVocabularyRepository, Set<String>> elementSetProvider )
		{
			this.text = text;
			this.elementSetProvider = elementSetProvider;
		}

		String getText()
		{
			return text;
		}

		Set<String> getElementSet( ControlledVocabularyRepository repository )
		{
			return elementSetProvider.apply( repository );
		}
	}
}
