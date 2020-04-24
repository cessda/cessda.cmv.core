package eu.cessda.cmv.core;

import static java.util.Optional.empty;

import java.util.Optional;
import java.util.Set;

class CodeValueOfControlledVocabularyValidator implements Validator.V10
{
	private String locationPath;
	private String codeValue;
	private String url;

	CodeValueOfControlledVocabularyValidator( String locationPath, String codeValue, String url )
	{
		this.locationPath = locationPath;
		this.codeValue = codeValue;
		this.url = url;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends ConstraintViolation> Optional<T> validate()
	{
		Set<String> elementSet = new DdiAnalysisUnit10InMemoryControlledVocabularyRepository().findCodeValues();
		if ( !elementSet.contains( codeValue ) )
		{
			Object cv = new CodeValueOfControlledVocabularyContraintViolation( locationPath, codeValue, url );
			return Optional.of( (T) cv );
		}
		else
		{
			return empty();
		}
	}
}
