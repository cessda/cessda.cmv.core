package eu.cessda.cmv.core.controlledvocabulary;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.io.File;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.junit.jupiter.api.Test;

class CessdaControlledVocabularyRepositoryTest
{
	private TestEnv.V13 testEnv = DefaultTestEnv.newInstance( CessdaControlledVocabularyRepositoryTest.class );

	@Test
	void findCodeValues_AnalysisUnit_20()
	{
		org.hamcrest.Matcher<Iterable<? extends String>> expectedValues = contains(
				"Group",
				"MediaUnit.Video",
				"PoliticalAdministrativeArea",
				"MediaUnit.Text",
				"OrganizationOrInstitution",
				"MediaUnit.Sound",
				"Household",
				"Family.HouseholdFamily",
				"HousingUnit",
				"GeographicUnit",
				"MediaUnit",
				"Individual",
				"TimeUnit",
				"Family",
				"MediaUnit.StillImage",
				"Object",
				"Other",
				"EventOrProcessOrActivity" );

		File file = testEnv.findTestResourceByName( "response-20200512.json" );
		CessdaControlledVocabularyRepository repository = new CessdaControlledVocabularyRepository( file.toURI() );
		assertThat( repository.findCodeValues(), hasSize( 18 ) );
		assertThat( repository.findCodeValues(), expectedValues );
	}

	@Test
	void findDescriptiveTerms_AnalysisUnit_20()
	{
		org.hamcrest.Matcher<Iterable<? extends String>> expectedValues = contains(
				"Group",
				"Political-administrative area",
				"Family: Household family",
				"Household",
				"Event/Process/Activity",
				"Media unit: Sound",
				"Media unit: Text",
				"Housing unit",
				"Individual",
				"Family",
				"Media unit",
				"Object",
				"Time unit",
				"Media unit: Still image",
				"Media unit: Video",
				"Organization/Institution",
				"Geographic unit",
				"Other" );

		File file = testEnv.findTestResourceByName( "response-20200512.json" );
		CessdaControlledVocabularyRepository repository = new CessdaControlledVocabularyRepository( file.toURI() );
		assertThat( repository.findDescriptiveTerms(), hasSize( 18 ) );
		assertThat( repository.findDescriptiveTerms(), expectedValues );
		assertThat( repository.getUri(), equalTo( file.toURI() ) );
	}
}
