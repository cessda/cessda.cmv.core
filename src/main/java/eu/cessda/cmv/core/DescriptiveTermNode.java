package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import org.gesis.commons.xml.LocationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepository;

class DescriptiveTermNode extends Node
{
	private static final Logger LOGGER = LoggerFactory.getLogger( DescriptiveTermNode.class );

	private ControlledVocabularyRepository.V11 controlledVocabularyRepository;

	DescriptiveTermNode( String locationPath,
			String textContent,
			Optional<LocationInfo> locationInfo,
			ControlledVocabularyRepository.V11 controlledVocabularyRepository )
	{
		super( locationPath, textContent, locationInfo );

		requireNonNull( controlledVocabularyRepository );
		this.controlledVocabularyRepository = controlledVocabularyRepository;

	}

	public ControlledVocabularyRepository.V11 getControlledVocabularyRepository()
	{
		return controlledVocabularyRepository;
	}
}
