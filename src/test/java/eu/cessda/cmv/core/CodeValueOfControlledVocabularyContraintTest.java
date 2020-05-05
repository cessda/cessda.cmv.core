package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.Factory.newDocument;
import static org.gesis.commons.test.hamcrest.OptionalMatchers.isEmpty;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Optional;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.junit.jupiter.api.Test;

public class CodeValueOfControlledVocabularyContraintTest
{
	private TestEnv.V13 testEnv = DefaultTestEnv.newInstance( CodeValueOfControlledVocabularyContraintTest.class );

	@Test
	public void validate_invalid() throws Exception
	{
		// given
		Document document = newDocument( testEnv.findTestResourceByName( "bad-case.xml" ) );

		// when
		String locationPath = "/codeBook/stdyDscr/stdyInfo/sumDscr/anlyUnit/concept";
		Constraint.V20 constraint = new CodeValueOfControlledVocabularyContraint( locationPath );
		List<Validator.V10> validators = constraint.newValidators( document );

		// then
		assertThat( validators, hasSize( 2 ) );
		Optional<ConstraintViolation> result;
		result = validators.get( 0 ).validate();
		assertThat( result, isEmpty() );
		result = validators.get( 1 ).validate();
		assertThat( result.get().getMessage(), containsString( "is not element of the controlled vocabulary in" ) );
	}

	@Test
	public void validate_valid() throws Exception
	{
		// given
		Document document = newDocument( testEnv.findTestResourceByName( "good-case.xml" ) );

		// when
		String locationPath = "/codeBook/stdyDscr/stdyInfo/sumDscr/anlyUnit/concept";
		Constraint.V20 constraint = new CodeValueOfControlledVocabularyContraint( locationPath );
		List<Validator.V10> validators = constraint.newValidators( document );

		// then
		assertThat( validators, hasSize( 2 ) );
		Optional<ConstraintViolation> result;
		result = validators.get( 0 ).validate();
		assertThat( result, isEmpty() );
		result = validators.get( 1 ).validate();
		assertThat( result, isEmpty() );
	}
}
