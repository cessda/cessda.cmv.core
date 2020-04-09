package eu.cessda.cmv.core;

import java.util.HashSet;
import java.util.Set;

class DdiAnalysisUnit10InMemoryControlledVocabularyRepository implements ControlledVocabularyRepository
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
