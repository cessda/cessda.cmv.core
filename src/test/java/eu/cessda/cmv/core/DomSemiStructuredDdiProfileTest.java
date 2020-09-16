package eu.cessda.cmv.core;

import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.io.FileMatchers.anExistingFile;

import java.io.File;
import java.net.URL;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import eu.cessda.cmv.core.mediatype.profile.v0.ProfileV0;

class DomSemiStructuredDdiProfileTest
{
	private CessdaMetadataValidatorFactory factory;

	DomSemiStructuredDdiProfileTest()
	{
		factory = new CessdaMetadataValidatorFactory();
	}

	@Test
	void construct_cdc122()
	{
		// given
		URL url = getClass().getResource( "/demo-documents/ddi-v25/cdc_122_profile.xml" );

		// when
		Profile.V10 profile = factory.newProfile( url );

		// then
		assertThat( countConstraints( profile, CompilableXPathConstraint.class ), is( 61 ) );
		assertThat( countConstraints( profile, PredicatelessXPathConstraint.class ), is( 61 ) );
		assertThat( countConstraints( profile, RecommendedNodeConstraint.class ), is( 24 ) );
		assertThat( countConstraints( profile, MandatoryNodeConstraint.class ), is( 10 ) );
		assertThat( countConstraints( profile, FixedValueNodeConstraint.class ), is( 5 ) );
		assertThat( countConstraints( profile, OptionalNodeConstraint.class ), is( 27 ) );
		assertThat( countConstraints( profile, MandatoryNodeIfParentPresentConstraint.class ), is( 15 ) );
		assertThat( profile.getConstraints(), hasSize( 61 + 61 + 24 + 10 + 5 + 27 + 15 ) );
	}

	@Test
	void construct_cdc25()
	{
		// given
		URL url = getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" );

		// when
		Profile.V10 profile = factory.newProfile( url );

		// then
		assertThat( countConstraints( profile, CompilableXPathConstraint.class ), is( 61 ) );
		assertThat( countConstraints( profile, PredicatelessXPathConstraint.class ), is( 61 ) );
		assertThat( countConstraints( profile, RecommendedNodeConstraint.class ), is( 24 ) );
		assertThat( countConstraints( profile, MandatoryNodeConstraint.class ), is( 10 ) );
		assertThat( countConstraints( profile, FixedValueNodeConstraint.class ), is( 5 ) );
		assertThat( countConstraints( profile, OptionalNodeConstraint.class ), is( 27 ) );
		assertThat( countConstraints( profile, MandatoryNodeIfParentPresentConstraint.class ), is( 15 ) );
		assertThat( profile.getConstraints(), hasSize( 61 + 61 + 24 + 10 + 5 + 27 + 15 ) );
	}

	private int countConstraints( Profile.V10 profile, Class<? extends Constraint> clazz )
	{
		return (int) profile.getConstraints().stream()
				.filter( constraint -> clazz.isInstance( constraint ) )
				.count();
	}

	@Test
	@Disabled( "Own profile format is not up-to-date" ) // TODO
	void toJaxbProfileV0()
	{
		// given
		URL sourceUrl = getClass().getResource( "/demo-documents/ddi-v25/cdc25_profile.xml" );
		File targetFile = new File( "target", "cdc25_profile_cmv.xml" );

		// when
		DomSemiStructuredDdiProfile profile = new DomSemiStructuredDdiProfile(
				factory.newDdiInputStream( newResource( sourceUrl ).readInputStream() ) );
		ProfileV0 jaxbProfile = profile.toJaxbProfileV0();
		jaxbProfile.saveAs( targetFile );
		assertThat( targetFile, anExistingFile() );
		assertThat( jaxbProfile.getConstraints(), hasSize( 183 ) ); // why not 203 ?
	}
}
