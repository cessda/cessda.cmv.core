package eu.cessda.cmv.core;

import java.util.List;

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepository;

public interface Document
{
	public interface V10 extends Document
	{
		public List<Node> getNodes( String locationPath );
	}

	public interface V11 extends Document.V10
	{
		public void register( String uri, ControlledVocabularyRepository controlledVocabularyRepository );

		public <T extends ControlledVocabularyRepository> T findControlledVocabularyRepository( String uri );
	}

	public interface V12 extends Document.V11
	{
		public void validate();
	}
}