package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

class CodeValueOfControlledVocabularyContraintViolation implements ConstraintViolation.V10
{
	private CodeValueNode node;

	CodeValueOfControlledVocabularyContraintViolation( CodeValueNode node )
	{
		requireNonNull( node );
		this.node = node;
	}

	@Override
	public String getMessage()
	{
		String message = "CodeValue '%s' in '%s' is not element of the controlled vocabulary in '%s'";
		return String.format( message,
				node.getTextContent(),
				node.getTextContent(),
				node.getControlledVocabularyRepositoryUri() );
	}
}
