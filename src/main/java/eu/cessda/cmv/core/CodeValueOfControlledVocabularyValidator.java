package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;
import java.util.Set;

class CodeValueOfControlledVocabularyValidator implements Validator.V10
{
	private CodeValueNode node;
	private ControlledVocabularyRepository controlledVocabularyRepository;

	CodeValueOfControlledVocabularyValidator( CodeValueNode node,
			ControlledVocabularyRepository controlledVocabularyRepository )
	{
		requireNonNull( node );
		requireNonNull( controlledVocabularyRepository );

		this.node = node;
		this.controlledVocabularyRepository = controlledVocabularyRepository;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		Set<String> elementSet = controlledVocabularyRepository.findCodeValues();
		if ( !elementSet.contains( node.getTextContent() ) )
		{
			String message = "CodeValue '%s' in '%s' is not element of the controlled vocabulary in '%s'";
			message = String.format( message,
					node.getTextContent(),
					node.getTextContent(),
					node.getControlledVocabularyRepositoryUri() );
			return of( new ConstraintViolation( message, node.getLineNumber(), node.getColumnNumber() ) );
		}
		else
		{
			return empty();
		}
	}
}
