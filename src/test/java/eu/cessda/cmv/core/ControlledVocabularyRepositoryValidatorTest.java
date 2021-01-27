package eu.cessda.cmv.core;

import static org.gesis.commons.test.hamcrest.OptionalMatchers.isEmpty;
import static org.gesis.commons.test.hamcrest.OptionalMatchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import eu.cessda.cmv.core.controlledvocabulary.CessdaControlledVocabularyRepositoryV2;
import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepositoryProxy;

class ControlledVocabularyRepositoryValidatorTest
{
	@Test
	void validate_valid()
	{
		// given
		String canonicalName = CessdaControlledVocabularyRepositoryV2.class.getCanonicalName();
		String uri = "https://vocabularies.cessda.eu/v2/vocabularies/AnalysisUnit/2.0?languageVersion=en-2.0";
		ControlledVocabularyRepositoryProxy proxy = new ControlledVocabularyRepositoryProxy( canonicalName, uri );

		// when
		Validator.V10 validator = new ControlledVocabularyRepositoryValidator( proxy );
		Optional<ConstraintViolation> constraintViolation = validator.validate();

		// then
		assertThat( constraintViolation, isEmpty() );
	}

	@Test
	void validate_invalid()
	{
		// given
		String canonicalName = CessdaControlledVocabularyRepositoryV2.class.getCanonicalName();
		String uri = "https://vocabularies.cessda.eu/v2/vocabularies/AnalysisUnit/10.0?languageVersion=en-10.0";
		ControlledVocabularyRepositoryProxy proxy = new ControlledVocabularyRepositoryProxy( canonicalName, uri );

		// when
		Validator.V10 validator = new ControlledVocabularyRepositoryValidator( proxy );
		Optional<ConstraintViolation> constraintViolation = validator.validate();

		// then
		assertThat( constraintViolation, isPresent() );
		assertThat( constraintViolation.get().getMessage(), containsString( "Resource not found" ) );
	}
}
