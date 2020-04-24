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
	@SuppressWarnings( "unchecked" )
	public <T extends ConstraintViolation> Optional<T> validate()
	{
		Set<String> elementSet = controlledVocabularyRepository.findCodeValues();
		if ( !elementSet.contains( node.getTextContent() ) )
		{
			return of( (T) new CodeValueOfControlledVocabularyContraintViolation( node ) );
		}
		else
		{
			return empty();
		}
	}
}
