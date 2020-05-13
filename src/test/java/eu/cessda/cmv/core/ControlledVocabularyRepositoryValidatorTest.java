package eu.cessda.cmv.core;

import static org.gesis.commons.test.hamcrest.OptionalMatchers.isEmpty;
import static org.gesis.commons.test.hamcrest.OptionalMatchers.isPresent;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepositoryProxy;

public class ControlledVocabularyRepositoryValidatorTest
{
	@Test
	public void validate_valid()
	{
		// given
		String canonicalName = "eu.cessda.cmv.core.controlledvocabulary.CessdaControlledVocabularyRepository";
		String uri = "https://vocabularies.cessda.eu/v1/vocabulary-details/AnalysisUnit/en/2.0";
		ControlledVocabularyRepositoryProxy proxy = new ControlledVocabularyRepositoryProxy( canonicalName, uri );

		// when
		Validator.V10 validator = new ControlledVocabularyRepositoryValidator( proxy );
		Optional<ConstraintViolation> constraintViolation = validator.validate();

		// then
		assertThat( constraintViolation, isEmpty() );
	}

	@Test
	public void validate_invalid()
	{
		// given
		String canonicalName = "eu.cessda.cmv.core.controlledvocabulary.CessdaControlledVocabularyRepository";
		String uri = "https://vocabularies.cessda.eu/v1/vocabulary-details/AnalysisUnit/en/10.0";
		ControlledVocabularyRepositoryProxy proxy = new ControlledVocabularyRepositoryProxy( canonicalName, uri );

		// when
		Validator.V10 validator = new ControlledVocabularyRepositoryValidator( proxy );
		Optional<ConstraintViolation> constraintViolation = validator.validate();

		// then
		assertThat( constraintViolation, isPresent() );
		assertThat( constraintViolation.get().getMessage(), containsString( "Resource not found" ) );
	}
}
