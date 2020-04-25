package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import org.gesis.commons.xml.LocationInfo;

class CodeValueNode extends Node
{
	private String controlledVocabularyRepositoryUri;

	CodeValueNode( String locationPath,
			String textContent,
			Optional<LocationInfo> locationInfo,
			String controlledVocabularyRepositoryUri )
	{
		super( locationPath, textContent, locationInfo );

		requireNonNull( controlledVocabularyRepositoryUri );
		this.controlledVocabularyRepositoryUri = controlledVocabularyRepositoryUri;

	}

	public String getControlledVocabularyRepositoryUri()
	{
		return controlledVocabularyRepositoryUri;
	}
}
