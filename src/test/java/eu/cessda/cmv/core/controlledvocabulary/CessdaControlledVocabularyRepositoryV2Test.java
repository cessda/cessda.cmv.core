package eu.cessda.cmv.core.controlledvocabulary;

import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.junit.jupiter.api.Test;

class CessdaControlledVocabularyRepositoryV2Test
{
	private TestEnv.V13 testEnv = DefaultTestEnv.newInstance( CessdaControlledVocabularyRepositoryV2Test.class );

	@Test
	void findCodeValues_AnalysisUnit_10()
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

		String url = "https://vocabularies.cessda.eu/v2/vocabularies/AnalysisUnit/1.0?languageVersion=en-1.0";
		Resource resource = newResource( url );
		CessdaControlledVocabularyRepositoryV2 repository = new CessdaControlledVocabularyRepositoryV2( resource );
		assertThat( repository.findCodeValues(), hasSize( 13 ) );
		assertThat( repository.findCodeValues(), expectedCodeValues );
		assertThat( repository.getUri(), equalTo( resource.getUri() ) );
	}

	@Test
	void findCodeValues_AnalysisUnit_20()
	{
		org.hamcrest.Matcher<Iterable<? extends String>> expectedCodeValues = containsInAnyOrder( "Group",
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

		File file = testEnv.findTestResourceByName( "response-20210127.json" );
		CessdaControlledVocabularyRepositoryV2 repository = new CessdaControlledVocabularyRepositoryV2( file.toURI() );
		assertThat( repository.findCodeValues(), hasSize( 18 ) );
		assertThat( repository.findCodeValues(), expectedCodeValues );

		String url = "https://vocabularies.cessda.eu/v2/vocabularies/AnalysisUnit/2.0?languageVersion=en-2.0";
		Resource resource = newResource( url );
		repository = new CessdaControlledVocabularyRepositoryV2( resource );
		assertThat( repository.findCodeValues(), hasSize( 18 ) );
		assertThat( repository.findCodeValues(), expectedCodeValues );
	}

	@Test
	void findDescriptiveTerms_AnalysisUnit_fi_202()
	{
		org.hamcrest.Matcher<Iterable<? extends String>> expectedCodeValues = containsInAnyOrder(
				"Perhe: perhekotitalous",
				"Eloton objekti/esine",
				"Muu havainto/aineistoyksikkötyyppi",
				"Henkilö",
				"Ryhmä",
				"Kotitalous",
				"Asuntokunta",
				"Tapahtuma/prosessi/toiminta",
				"Mediatyyppi: liikkuva kuva",
				"Mediatyyppi: teksti",
				"Organisaatio",
				"Mediatyyppi (teksti, ääni, still-kuva, liikkuva kuva)",
				"Perhe",
				"Aikajakso",
				"Maantieteellinen yksikkö",
				"Mediatyyppi: ääni",
				"Mediatyyppi: still-kuva",
				"Poliittis-hallinnollinen alue" );

		String url = "https://vocabularies.cessda.eu/v2/vocabularies/AnalysisUnit/2.0?languageVersion=fi-2.0.2";
		Resource resource = newResource( url );
		CessdaControlledVocabularyRepositoryV2 repository = new CessdaControlledVocabularyRepositoryV2( resource );
		assertThat( repository.findDescriptiveTerms(), hasSize( 18 ) );
		assertThat( repository.findDescriptiveTerms(), expectedCodeValues );
	}

	@Test
	void findDescriptiveTerms_AnalysisUnit_de_101()
	{
		org.hamcrest.Matcher<Iterable<? extends String>> expectedCodeValues = contains( "Geographische Einheit",
				"Ereignis/ Prozess",
				"Organisation",
				"Texteinheit",
				"Haushalt",
				"Zeiteinheit",
				"Individuum",
				"Sonstiges",
				"Familie: Familienhaushalt",
				"Objekt",
				"Wohneinheit",
				"Familie",
				"Gruppe" );

		String url = "https://vocabularies.cessda.eu/v2/vocabularies/AnalysisUnit/1.0?languageVersion=de-1.0.1";
		Resource resource = newResource( url );
		CessdaControlledVocabularyRepositoryV2 repository = new CessdaControlledVocabularyRepositoryV2( resource );
		assertThat( repository.findDescriptiveTerms(), hasSize( 13 ) );
		assertThat( repository.findDescriptiveTerms(), expectedCodeValues );
	}

	@Test
	void construct_ResourceNotFound()
	{
		String url = "https://vocabularies.cessda.eu/v2/vocabularies/AnalysisUnit/0.0?languageVersion=en-0.0";
		Resource resource = newResource( url );
		Exception exception = assertThrows( Exception.class,
				() -> new CessdaControlledVocabularyRepositoryV2( resource ) );
		assertThat( exception.getMessage(), containsString( "Resource not found" ) );
	}
}
