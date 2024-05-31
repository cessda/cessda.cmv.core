/*-
 * #%L
 * cmv-core
 * %%
 * Copyright (C) 2020 - 2024 CESSDA ERIC
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
package eu.cessda.cmv.core.mediatype.profile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.junit.jupiter.api.Test;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.LOWER_CAMEL_CASE;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.UPPER_CAMEL_CASE;
import static eu.cessda.cmv.core.mediatype.profile.Profile.SCHEMALOCATION_FILENAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

class ProfileTest
{
	private final TestEnv.V11 testEnv;

	ProfileTest()
	{
		testEnv = DefaultTestEnv.newInstance( ProfileTest.class );
	}

	private Profile newProfile()
	{
		Profile profile = new Profile();
		profile.setName( "Test Profile" );
		profile.setVersion( "1.0" );

		FixedValueNodeConstraint fixedValueNodeConstraint = new FixedValueNodeConstraint();
		fixedValueNodeConstraint.setFixedValue( "Fixed value" );
		fixedValueNodeConstraint.setLocationPath( "/fixed/value/node/location/path" );

		MandatoryNodeConstraint mandatoryNodeConstraint = new MandatoryNodeConstraint();
		mandatoryNodeConstraint.setLocationPath( "/mandatory/node/location/path" );

		MandatoryNodeIfParentPresentConstraint mandatoryNodeIfParentPresentConstraint = new MandatoryNodeIfParentPresentConstraint();
		mandatoryNodeIfParentPresentConstraint.setLocationPath( "/mandatory/node/if/parent/present/location/path" );

		NotBlankNodeConstraint notBlankNodeConstraint = new NotBlankNodeConstraint();
		notBlankNodeConstraint.setLocationPath( "/not/blank/node/constraint" );

		OptionalNodeConstraint optionalNodeConstraint = new OptionalNodeConstraint();
		optionalNodeConstraint.setLocationPath( "/optional/node/constraint" );

		PredicatelessXPathConstraint predicatelessXPathConstraint = new PredicatelessXPathConstraint();
		predicatelessXPathConstraint.setLocationPath( "/predicateless/xpath/node/constraint" );

		RecommendedNodeConstraint recommendedNodeConstraint = new RecommendedNodeConstraint();
		recommendedNodeConstraint.setLocationPath( "/recommended/node/constraint" );

		profile.setConstraints( Arrays.asList(
			new CompilableXPathConstraint(),
			fixedValueNodeConstraint,
			mandatoryNodeConstraint,
			mandatoryNodeIfParentPresentConstraint,
			notBlankNodeConstraint,
			optionalNodeConstraint,
			predicatelessXPathConstraint,
			recommendedNodeConstraint
		) );

		PrefixMap ddiPrefixMap = new PrefixMap();
		ddiPrefixMap.setPrefix( "ddi" );
		ddiPrefixMap.setNamespace( "ddi:codebook:2_5" );

		PrefixMap xsiPrefixMap = new PrefixMap();
		xsiPrefixMap.setPrefix( "xsi" );
		xsiPrefixMap.setNamespace( "http://www.w3.org/2001/XMLSchema-instance" );

		profile.setPrefixMap( Arrays.asList( ddiPrefixMap, xsiPrefixMap ) );

		return profile;
	}

	@Test
	void writeAndReadWithEclipselinkMoxy()
	{
		Profile profile = newProfile();

		File file = new File( testEnv.newDirectory(), "eclipselink-moxy.xml" );

		// Read and write the report to an XML file
		profile.saveAs( file );

		// Compare the report to the expected XML
		assertThat( new StreamSource( file ), isSimilarTo( new StreamSource( testEnv.findTestResourceByName( "eclipselink-moxy.xml" ) ) ).ignoreWhitespace() );
	}

	@Test
	void writeAndReadWithJackson() throws IOException
	{
		writeAndReadWithJackson( new File("jackson.json" ), LOWER_CAMEL_CASE, new ObjectMapper() );

		XmlMapper objectMapper = new XmlMapper();
		objectMapper.registerModule( new JaxbAnnotationModule() );
		writeAndReadWithJackson( new File( "jackson.xml" ), UPPER_CAMEL_CASE, objectMapper );
	}

	private void writeAndReadWithJackson( File file, PropertyNamingStrategy propertyNamingStrategy, ObjectMapper objectMapper ) throws IOException
	{
		objectMapper.setPropertyNamingStrategy( propertyNamingStrategy );

		Profile profile = newProfile();

		// Serialise the result to a string
		String content = objectMapper.writeValueAsString( profile );

		// Parse the string into a JSON tree, and compare with the reference file
		// Direct string comparisons are unreliable (e.g. line endings, differences in serialisation)
		JsonNode actualTree = objectMapper.readTree( content );
		JsonNode expectedTree = objectMapper.readTree( testEnv.findTestResourceByName( file.getName() ) );
		assertThat( actualTree, equalTo( expectedTree ) );
	}

	@Test
	void testGenerateSchema()
	{
		File expectedFile = testEnv.findTestResourceByName( SCHEMALOCATION_FILENAME );
		File actualFile = new File( testEnv.newDirectory(), SCHEMALOCATION_FILENAME );

		Profile.generateSchema( actualFile );

		// Compare the generated file to the expected file
		assertThat( new StreamSource( actualFile ), isSimilarTo( new StreamSource( expectedFile ) ).ignoreWhitespace() );
	}
}
