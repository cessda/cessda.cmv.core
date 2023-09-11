/*-
 * #%L
 * cmv-core
 * %%
 * Copyright (C) 2020 - 2021 CESSDA ERIC
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package eu.cessda.cmv.core.controlledvocabulary;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CessdaControlledVocabularyRepositoryV2Test
{
	private final TestEnv.V13 testEnv = DefaultTestEnv.newInstance( CessdaControlledVocabularyRepositoryV2Test.class );

	@Test
	void findCodeValues_AnalysisUnit_10()
	{
		Matcher<Iterable<? extends String>> expectedCodeValues = contains(
			"Group",
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
			"Other"
		);

		// Sourced from https://vocabularies.cessda.eu/v2/vocabularies/AnalysisUnit/1.0?languageVersion=en-1.0
		File analysisUnitJSON = testEnv.findTestResourceByName( "AnalysisUnit-en-1.0.json" );
		Resource resource = newResource( analysisUnitJSON );
		CessdaControlledVocabularyRepositoryV2 repository = new CessdaControlledVocabularyRepositoryV2( resource );
		assertThat( repository.findCodeValues(), hasSize( 13 ) );
		assertThat( repository.findCodeValues(), expectedCodeValues );
		assertThat( repository.getUri(), equalTo( resource.getUri() ) );
	}

	@Test
	void findCodeValues_AnalysisUnit_20()
	{
		Matcher<Iterable<? extends String>> expectedCodeValues = containsInAnyOrder(
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
			"EventOrProcessOrActivity"
		);

		File file = testEnv.findTestResourceByName( "response-20210127.json" );
		CessdaControlledVocabularyRepositoryV2 repository = new CessdaControlledVocabularyRepositoryV2( file.toURI() );
		assertThat( repository.findCodeValues(), hasSize( 18 ) );
		assertThat( repository.findCodeValues(), expectedCodeValues );

		// Sourced from https://vocabularies.cessda.eu/v2/vocabularies/AnalysisUnit/2.0?languageVersion=en-2.0
		File analysisUnitJSON = testEnv.findTestResourceByName( "AnalysisUnit-en-2.0.json" );
		Resource resource = newResource( analysisUnitJSON );
		repository = new CessdaControlledVocabularyRepositoryV2( resource );
		assertThat( repository.findCodeValues(), hasSize( 18 ) );
		assertThat( repository.findCodeValues(), expectedCodeValues );
	}

	@Test
	void findDescriptiveTerms_AnalysisUnit_fi_202()
	{
		Matcher<Iterable<? extends String>> expectedCodeValues = containsInAnyOrder(
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
				"Poliittis-hallinnollinen alue"
		);

		// Sourced from https://vocabularies.cessda.eu/v2/vocabularies/AnalysisUnit/2.0?languageVersion=fi-2.0.2
		File analysisUnitJSON = testEnv.findTestResourceByName( "AnalysisUnit-fi-2.0.2.json" );
		Resource resource = newResource( analysisUnitJSON );
		CessdaControlledVocabularyRepositoryV2 repository = new CessdaControlledVocabularyRepositoryV2( resource );
		assertThat( repository.findDescriptiveTerms(), hasSize( 18 ) );
		assertThat( repository.findDescriptiveTerms(), expectedCodeValues );
	}

	@Test
	void findDescriptiveTerms_AnalysisUnit_de_101()
	{
		Matcher<Iterable<? extends String>> expectedCodeValues = contains(
			"Geographische Einheit",
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
			"Gruppe"
		);

		// Sourced from https://vocabularies.cessda.eu/v2/vocabularies/AnalysisUnit/1.0?languageVersion=de-1.0.1
		File analysisUnitJSON = testEnv.findTestResourceByName( "AnalysisUnit-de-1.0.1.json" );
		Resource resource = newResource( analysisUnitJSON );
		CessdaControlledVocabularyRepositoryV2 repository = new CessdaControlledVocabularyRepositoryV2( resource );
		assertThat( repository.findDescriptiveTerms(), hasSize( 13 ) );
		assertThat( repository.findDescriptiveTerms(), expectedCodeValues );
	}

	@Test
	void construct_ResourceNotFound()
	{
		String url = "http://localhost/v2/vocabularies/AnalysisUnit/0.0?languageVersion=en-0.0";
		Resource resource = newResource( url );
		Exception exception = assertThrows( Exception.class,
				() -> new CessdaControlledVocabularyRepositoryV2( resource ) );
		assertThat( exception.getMessage(), containsString( "Resource not found" ) );
	}
}
