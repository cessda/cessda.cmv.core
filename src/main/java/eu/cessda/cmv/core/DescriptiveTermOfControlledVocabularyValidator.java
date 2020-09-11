package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.ElementOfControlledVocabularyValidator.ElementName.DESCRIPTIVE_TERM;

class DescriptiveTermOfControlledVocabularyValidator extends ElementOfControlledVocabularyValidator
{
	DescriptiveTermOfControlledVocabularyValidator( ControlledVocabularyNode node )
	{
		super( node, DESCRIPTIVE_TERM );
	}
}
