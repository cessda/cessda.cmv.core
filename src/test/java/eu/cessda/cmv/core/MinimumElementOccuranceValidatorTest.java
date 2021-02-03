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

import static org.gesis.commons.test.hamcrest.OptionalMatchers.isEmpty;
import static org.gesis.commons.test.hamcrest.OptionalMatchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class MinimumElementOccuranceValidatorTest
{
	@Test
	void validateValid()
	{
		// given
		long actualOccurs = 1;
		long minOccurs = 1;
		Validator.V10 validator = new MinimumElementOccuranceValidator( "/path/to/element", actualOccurs, minOccurs );

		// when
		Optional<ConstraintViolation> constraintViolation = validator.validate();

		// then
		assertThat( constraintViolation, isEmpty() );
	}

	@Test
	void validateInvalid()
	{
		// given
		long actualOccurs = 7;
		long minOccurs = 8;
		Validator.V10 validator = new MinimumElementOccuranceValidator( "/path/to/element", actualOccurs, minOccurs );

		// when
		Optional<ConstraintViolation> constraintViolation = validator.validate();

		// then
		assertThat( constraintViolation, isPresent() );
		assertThat( constraintViolation.get().getMessage(), containsString( "deceeds minimal count" ) );
	}

	@Test
	void construct()
	{
		assertThrows( NullPointerException.class, () -> new MinimumElementOccuranceValidator( null, 1, 1 ) );
		assertThrows( IllegalArgumentException.class,
				() -> new MaximumElementOccuranceValidator( "/path/to/element", -1, 1 ) );
		assertThrows( IllegalArgumentException.class,
				() -> new MaximumElementOccuranceValidator( "/path/to/element", 1, -1 ) );
	}
}
