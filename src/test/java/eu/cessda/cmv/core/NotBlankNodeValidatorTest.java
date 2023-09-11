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

class NotBlankNodeValidatorTest
{
	@Test
	void validate_empty()
	{
		// given: empty text content
		Node node = new Node( "/path/to/node", "", null );
		Validator validator = new NotBlankNodeValidator( node );

		// when
		Optional<ConstraintViolation> constraintViolation = validator.validate();

		// then
		assertThat( constraintViolation, isPresent() );
	}

	@Test
	void validate_blank()
	{
		// given: blank text content
		Node node = new Node( "/path/to/node", "   ", null );
		Validator validator = new NotBlankNodeValidator( node );

		// when
		Optional<ConstraintViolation> constraintViolation = validator.validate();

		// then
		assertThat( constraintViolation, isPresent() );
	}

	@Test
	void validate_notBlank()
	{
		// given: not blank text content
		Node node = new Node( "/path/to/node", "not blank", null );
		Validator validator = new NotBlankNodeValidator( node );

		// when
		Optional<ConstraintViolation> constraintViolation = validator.validate();

		// then
		assertThat( constraintViolation, isEmpty() );
	}
}
