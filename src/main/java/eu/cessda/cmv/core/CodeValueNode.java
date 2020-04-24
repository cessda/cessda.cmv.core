package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

class CodeValueNode extends Node
{
	private String controlledVocabularyRepositoryUri;

	public CodeValueNode( String locationPath, String textContent )
	{
		this( locationPath, textContent, null );
	}

	CodeValueNode( String locationPath, String textContent,
			String controlledVocabularyRepositoryUri )
	{
		super( locationPath, textContent );
		requireNonNull( controlledVocabularyRepositoryUri );
		this.controlledVocabularyRepositoryUri = controlledVocabularyRepositoryUri;
	}

	public String getControlledVocabularyRepositoryUri()
	{
		return controlledVocabularyRepositoryUri;
	}
}
