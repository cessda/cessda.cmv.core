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
package eu.cessda.cmv.core;

import eu.cessda.cmv.core.mediatype.profile.Profile;
import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.gesis.commons.xml.XMLDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

class DomSemiStructuredDdiProfileTest
{
	private final CessdaMetadataValidatorFactory factory = new CessdaMetadataValidatorFactory();
	private final TestEnv.V14 testEnv = DefaultTestEnv.newInstance( DomSemiStructuredDdiProfileTest.class );

	@ParameterizedTest
	@ValueSource(
			strings = { "/demo-documents/ddi-v25/cdc25_profile.xml", "/demo-documents/ddi-v25/cdc_122_profile.xml" } )
	void constructMultilingualProfiles( String classpathLocation ) throws NotDocumentException
	{
		// given
		URL url = getClass().getResource( classpathLocation );

		// when
		eu.cessda.cmv.core.Profile profile = factory.newProfile( url );

		// then
		assertThat( countConstraints( profile, CompilableXPathConstraint.class ), is( 61 ) );
		assertThat( countConstraints( profile, PredicatelessXPathConstraint.class ), is( 61 ) );
		assertThat( countConstraints( profile, RecommendedNodeConstraint.class ), is( 27 ) );
		assertThat( countConstraints( profile, MandatoryNodeConstraint.class ), is( 10 ) );
		assertThat( countConstraints( profile, FixedValueNodeConstraint.class ), is( 5 ) );
		assertThat( countConstraints( profile, OptionalNodeConstraint.class ), is( 24 ) );
		assertThat( countConstraints( profile, MandatoryNodeIfParentPresentConstraint.class ), is( 12 ) );
		assertThat( profile.getConstraints(), hasSize( 61 + 61 + 27 + 10 + 5 + 24 + 12 ) );
	}

	@ParameterizedTest
	@ValueSource(
			strings = { "/demo-documents/ddi-v25/cdc25_profile_mono.xml",
					"/demo-documents/ddi-v25/cdc_122_profile_mono.xml" } )
	void constructMonolingualProfiles( String classpathLocation ) throws NotDocumentException
	{
		// given
		URL url = getClass().getResource( classpathLocation );

		// when
		eu.cessda.cmv.core.Profile profile = factory.newProfile( url );

		// then
		assertThat( countConstraints( profile, CompilableXPathConstraint.class ), is( 44 ) );
		assertThat( countConstraints( profile, PredicatelessXPathConstraint.class ), is( 44 ) );
		assertThat( countConstraints( profile, RecommendedNodeConstraint.class ), is( 22 ) );
		assertThat( countConstraints( profile, MandatoryNodeConstraint.class ), is( 8 ) );
		assertThat( countConstraints( profile, FixedValueNodeConstraint.class ), is( 5 ) );
		assertThat( countConstraints( profile, OptionalNodeConstraint.class ), is( 14 ) );
		assertThat( countConstraints( profile, MandatoryNodeIfParentPresentConstraint.class ), is( 2 ) );
		assertThat( profile.getConstraints(), hasSize( 44 + 44 + 22 + 8 + 5 + 14 + 2 ) );
	}

	private int countConstraints( eu.cessda.cmv.core.Profile profile, Class<? extends Constraint> clazz )
	{
		return (int) profile.getConstraints().stream().filter( clazz::isInstance ).count();
	}

	@Test
	void toJaxbProfile() throws IOException, NotDocumentException, SAXException
	{
		// given
		URL sourceUrl = getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" );

		URL expectedFile = getClass().getResource( "/profiles/cdc25_profile_cmv.xml" );
		File actualFile = new File( testEnv.newDirectory(), "cdc25_profile_cmv.xml" );

		assert expectedFile != null;
		assert sourceUrl != null;

		// when
		InputSource inputSource = new InputSource( sourceUrl.toExternalForm() );
		DomSemiStructuredDdiProfile profile = new DomSemiStructuredDdiProfile( XMLDocument.newBuilder().build( inputSource ) );
		Profile jaxbProfile = profile.toJaxbProfile();
		assertThat( jaxbProfile.getConstraints(), hasSize( 61 + 61 + 27 + 10 + 5 + 24 + 12 ) );
		jaxbProfile.saveAs( actualFile );

		// Compare the generated file to the expected file
        assertThat( new StreamSource( actualFile ), isSimilarTo( new StreamSource( expectedFile.toExternalForm() ) ).ignoreWhitespace() );
	}
}
