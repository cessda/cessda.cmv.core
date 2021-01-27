package eu.cessda.cmv.core;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepository;

abstract class ElementOfControlledVocabularyValidator implements Validator.V10
{
	private ControlledVocabularyNode node;
	private ElementName elementName;

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
		ControlledVocabularyRepository.V11 repository = node.getControlledVocabularyRepository();
		if ( repository == null )
		{
			String message = format( "%s '%s' in '%s' is not validateable because no controlled vocabulary is declared",
					elementName.getText(), node.getTextContent(), node.getLocationPath() );
			return of( new ConstraintViolation( message, node.getLocationInfo() ) );
		}
		else
		{
			Set<String> elementSet = elementName.getElementSetProvider().apply( repository );
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
		CODE_VALUE("Code value", ControlledVocabularyRepository.V11::findCodeValues),
		DESCRIPTIVE_TERM("Descriptive term", ControlledVocabularyRepository.V11::findDescriptiveTerms);

		private String text;
		private Function<ControlledVocabularyRepository.V11, Set<String>> elementSetProvider;

		ElementName( String text, Function<ControlledVocabularyRepository.V11, Set<String>> elementSetProvider )
		{
			this.text = text;
			this.elementSetProvider = elementSetProvider;
		}

		String getText()
		{
			return text;
		}

		Function<ControlledVocabularyRepository.V11, Set<String>> getElementSetProvider()
		{
			return elementSetProvider;
		}
	}
}
