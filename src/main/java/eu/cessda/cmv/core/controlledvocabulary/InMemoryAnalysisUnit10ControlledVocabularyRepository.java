package eu.cessda.cmv.core.controlledvocabulary;

import java.util.HashSet;
import java.util.Set;

public class InMemoryAnalysisUnit10ControlledVocabularyRepository implements ControlledVocabularyRepository.V10
{
	@Override
	public Set<String> findCodeValues()
	{
		Set<String> codeValues = new HashSet<>();
		codeValues.add( "Individual" );
		codeValues.add( "Organization" );
		codeValues.add( "Family" );
		codeValues.add( "Family.HouseholdFamily" );
		codeValues.add( "Household" );
		codeValues.add( "HousingUnit" );
		codeValues.add( "EventOrProcess" );
		codeValues.add( "GeographicUnit" );
		codeValues.add( "TimeUnit" );
		codeValues.add( "TextUnit" );
		codeValues.add( "Group" );
		codeValues.add( "Object" );
		codeValues.add( "Other" );
		return codeValues;
	}
}
