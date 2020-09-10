package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;
import java.util.Set;

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepository;

class CodeValueOfControlledVocabularyValidator implements Validator.V10
{
	private CodeValueNode node;

	CodeValueOfControlledVocabularyValidator( CodeValueNode node )
	{
		requireNonNull( node );
		this.node = node;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		ControlledVocabularyRepository.V11 repository = node.getControlledVocabularyRepository();
		if ( repository == null )
		{
			String message = "Code value '%s' in '%s' is not validateable because no controlled vocabulary is declared";
			message = String.format( message, node.getTextContent(), node.getLocationPath() );
			return of( new ConstraintViolation( message, node.getLocationInfo() ) );
		}
		else
		{
			Set<String> elementSet = repository.findCodeValues();
			if ( !elementSet.contains( node.getTextContent() ) )
			{
				String message = "CodeValue '%s' in '%s' is not element of the controlled vocabulary in '%s'";
				message = String.format( message, node.getTextContent(), node.getLocationPath(), repository.getUri() );
				return of( new ConstraintViolation( message, node.getLocationInfo() ) );
			}
			else
			{
				return empty();
			}
		}
	}
}
