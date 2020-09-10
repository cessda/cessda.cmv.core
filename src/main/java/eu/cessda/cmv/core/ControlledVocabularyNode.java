package eu.cessda.cmv.core;

import java.util.Optional;

import org.gesis.commons.xml.LocationInfo;

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepository;

class ControlledVocabularyNode extends Node
{
	private ControlledVocabularyRepository.V11 controlledVocabularyRepository;

	ControlledVocabularyNode( String locationPath,
			String textContent,
			Optional<LocationInfo> locationInfo,
			ControlledVocabularyRepository.V11 controlledVocabularyRepository )
	{
		super( locationPath, textContent, locationInfo );
		this.controlledVocabularyRepository = controlledVocabularyRepository;
	}

	ControlledVocabularyRepository.V11 getControlledVocabularyRepository()
	{
		return controlledVocabularyRepository;
	}
}
