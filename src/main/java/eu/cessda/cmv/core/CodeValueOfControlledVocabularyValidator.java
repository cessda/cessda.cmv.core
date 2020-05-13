package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;
import java.util.Set;

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
		Set<String> elementSet = node.getControlledVocabularyRepository().findCodeValues();
		if ( !elementSet.contains( node.getTextContent() ) )
		{
			String message = "CodeValue '%s' in '%s' is not element of the controlled vocabulary in '%s'";
			message = String.format( message,
					node.getTextContent(),
					node.getLocationPath(),
					"TODO node.getControlledVocabularyRepositoryUri()" );
			return of( new ConstraintViolation( message, node.getLocationInfo() ) );
		}
		else
		{
			return empty();
		}
	}
}
