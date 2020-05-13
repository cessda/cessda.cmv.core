package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import org.gesis.commons.xml.LocationInfo;

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepository;

class CodeValueNode extends Node
{
	private ControlledVocabularyRepository.V10 controlledVocabularyRepository;

	CodeValueNode( String locationPath,
			String textContent,
			Optional<LocationInfo> locationInfo,
			ControlledVocabularyRepository.V10 controlledVocabularyRepository )
	{
		super( locationPath, textContent, locationInfo );

		requireNonNull( controlledVocabularyRepository );
		this.controlledVocabularyRepository = controlledVocabularyRepository;

	}

	public ControlledVocabularyRepository.V10 getControlledVocabularyRepository()
	{
		return controlledVocabularyRepository;
	}
}
