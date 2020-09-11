package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.ElementOfControlledVocabularyValidator.ElementName.CODE_VALUE;

class CodeValueOfControlledVocabularyValidator extends ElementOfControlledVocabularyValidator
{
	CodeValueOfControlledVocabularyValidator( ControlledVocabularyNode node )
	{
		super( node, CODE_VALUE );
	}
}
