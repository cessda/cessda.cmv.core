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

import java.util.Collections;
import java.util.List;

import static eu.cessda.cmv.core.ValidationGateName.BASIC;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BasicValidationGateTest
{
	private final CessdaMetadataValidatorFactory factory;

	BasicValidationGateTest()
	{
		factory = new CessdaMetadataValidatorFactory();
	}

	@Test
	void invalid()
	{
		// given
		Document document = mock( Document.class );
		when( document.getNodes( anyString() ) ).thenReturn( emptyList() );
		Profile profile = mock( Profile.class );
		when( profile.getConstraints() ).thenReturn( asList(
				new MandatoryNodeConstraint( "/path/to/mandatory/node" ),
				new RecommendedNodeConstraint( "/path/to/recommended/node" ) ) );

		// when
		ValidationGate validationGate = factory.newValidationGate( BASIC );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 1 ) );
	}

	@Test
	void valid()
	{
		// given
		Document document = mock( Document.class );
		when( document.getNodes( "/path/to/mandatory/node" ) )
				.thenReturn( Collections.singletonList( new Node( "/path/to/mandatory/node", "at-least-one", null ) ) );
		Profile profile = mock( Profile.class );
		when( profile.getConstraints() ).thenReturn( asList(
				new MandatoryNodeConstraint( "/path/to/mandatory/node" ),
				new RecommendedNodeConstraint( "/path/to/recommended/node" ) ) );

		// when
		ValidationGate validationGate = factory.newValidationGate( BASIC );
		List<ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// then
		assertThat( constraintViolations, hasSize( 0 ) );
	}
}
