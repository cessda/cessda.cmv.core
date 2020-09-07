package eu.cessda.cmv.core;

import static java.lang.Long.valueOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.junit.jupiter.api.Test;

class DescriptiveTermOfControlledVocabularyConstraintTest
{
	private TestEnv.V13 testEnv;
	private CessdaMetadataValidatorFactory factory;

	DescriptiveTermOfControlledVocabularyConstraintTest()
	{
		testEnv = DefaultTestEnv.newInstance( DescriptiveTermOfControlledVocabularyConstraintTest.class );
		factory = new CessdaMetadataValidatorFactory();
	}

	@Test
	void validate_valid() throws Exception
	{
		// given
		Document.V11 document = factory.newDocument( testEnv.findTestResourceByName( "good-case.xml" ) );
		Profile.V10 profile = factory.newProfile( testEnv.findTestResourceByName( "profile.xml" ).toURI().toURL() );

		// when
		ValidationGate.V10 validationGate = new StandardValidationGate();
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( profile.getConstraints().stream()
				.filter( cv -> cv instanceof ControlledVocabularyRepositoryConstraint )
				.count(), is( valueOf( 4 ) ) );
		assertThat( profile.getConstraints().stream()
				.filter( cv -> cv instanceof DescriptiveTermOfControlledVocabularyConstraint )
				.count(), is( valueOf( 1 ) ) );
		assertThat( constraintViolations, hasSize( 0 ) );
	}
}
