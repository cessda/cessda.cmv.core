package eu.cessda.cmv.core.controlledvocabulary;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.net.URL;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.junit.jupiter.api.Test;

class CessdaControlledVocabularyRepositoryTest
{
	private TestEnv.V13 testEnv = DefaultTestEnv.newInstance( CessdaControlledVocabularyRepositoryTest.class );

	@Test
	void findCodeValues_AnalysisUnit_10() throws Exception
	{
		org.hamcrest.Matcher<Iterable<? extends String>> expectedCodeValues = contains( "Group",
				"Organization",
				"Household",
				"Family.HouseholdFamily",
				"EventOrProcess",
				"HousingUnit",
				"GeographicUnit",
				"Individual",
				"Family",
				"TimeUnit",
				"TextUnit",
				"Object",
				"Other" );

		URL url = new URL( "https://vocabularies.cessda.eu/v1/vocabulary-details/AnalysisUnit/en/1.0" );
		CessdaControlledVocabularyRepository repository = new CessdaControlledVocabularyRepository( url.toURI() );
		assertThat( repository.findCodeValues(), hasSize( 13 ) );
		assertThat( repository.findCodeValues(), expectedCodeValues );
	}

	@Test
	void findCodeValues_AnalysisUnit_20() throws Exception
	{
		org.hamcrest.Matcher<Iterable<? extends String>> expectedCodeValues = contains( "Group",
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
		assertThat( repository.findCodeValues(), expectedCodeValues );

		URL url = new URL( "https://vocabularies.cessda.eu/v1/vocabulary-details/AnalysisUnit/en/2.0" );
		repository = new CessdaControlledVocabularyRepository( url.toURI() );
		assertThat( repository.findCodeValues(), hasSize( 18 ) );
		assertThat( repository.findCodeValues(), expectedCodeValues );
	}

	@Test
	void construct_ResourceNotFound()
	{
		Exception exception = assertThrows( Exception.class, () ->
		{
			URL notFoundUrl = new URL( "https://vocabularies.cessda.eu/v1/vocabulary-details/AnalysisUnit/en/10.0" );
			new CessdaControlledVocabularyRepository( notFoundUrl.toURI() );
		} );
		assertThat( exception.getMessage(), containsString( "Resource not found" ) );
	}
}
