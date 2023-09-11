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

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.gesis.commons.test.hamcrest.OptionalMatchers.isEmpty;
import static org.gesis.commons.test.hamcrest.OptionalMatchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MaximumElementOccurrenceValidatorTest
{
	@Test
	void validateValid()
	{
		// given
		Validator validator = new MaximumElementOccurrenceValidator( "/some/path/to/element", 5, 6 );
		// when
		Optional<ConstraintViolation> constraintViolation = validator.validate();
		// then
		assertThat( constraintViolation, isEmpty() );
	}

	@Test
	void validateInvalid()
	{
		// given
		Validator validator = new MaximumElementOccurrenceValidator( "/some/path/to/element", 7, 6 );
		// when
		Optional<ConstraintViolation> constraintViolation = validator.validate();
		// then
		assertThat( constraintViolation, isPresent() );
		assertThat( constraintViolation.get().getMessage(), containsString( "occurs more than maximal count of" ) );
	}

	@Test
	void construct()
	{
		assertThrows( NullPointerException.class, () -> new MaximumElementOccurrenceValidator( null, 1, 1 ) );
		assertThrows( IllegalArgumentException.class,
				() -> new MaximumElementOccurrenceValidator( "/some/path/to/element", -1, 1 ) );
		assertThrows( IllegalArgumentException.class,
				() -> new MaximumElementOccurrenceValidator( "/some/path/to/element", 1, -1 ) );
	}
}
