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
import org.gesis.commons.resource.io.DdiInputStream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.io.FileMatchers.anExistingFile;

class DomSemiStructuredDdiProfileTest
{
	private final CessdaMetadataValidatorFactory factory;

	DomSemiStructuredDdiProfileTest()
	{
		factory = new CessdaMetadataValidatorFactory();
	}

	@ParameterizedTest
	@ValueSource(
			strings = { "/demo-documents/ddi-v25/cdc25_profile.xml", "/demo-documents/ddi-v25/cdc_122_profile.xml" } )
	void constructMultilingualProfiles( String classpathLocation ) throws IOException
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
	void constructMonolingualProfiles( String classpathLocation ) throws IOException
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
	@Disabled( "Own profile format is not up-to-date" ) // TODO
	void toJaxbProfileV0() throws IOException
	{
		// given
		URL sourceUrl = getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" );
		File targetFile = new File( "target", "cdc25_profile_cmv.xml" );

		// when
		InputStream inputStream = newResource( Objects.requireNonNull( sourceUrl ) ).readInputStream();
		DomSemiStructuredDdiProfile profile = new DomSemiStructuredDdiProfile(
			new DdiInputStream( inputStream ) );
		Profile jaxbProfile = profile.toJaxbProfileV0();
		jaxbProfile.saveAs( targetFile );
		assertThat( targetFile, anExistingFile() );
		assertThat( jaxbProfile.getConstraints(), hasSize( 183 ) ); // why not 203 ?
	}
}
