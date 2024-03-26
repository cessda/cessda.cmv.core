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

import org.hamcrest.Matcher;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.gesis.commons.test.hamcrest.OptionalMatchers.isEmpty;
import static org.gesis.commons.test.hamcrest.OptionalMatchers.isPresent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PredicatelessXPathConstraintTest
{
	private static final String newTestParameters = "newTestParameters";

	private static class TestParameter
	{
		String locationPath;
		Matcher<Optional<?>> expectedConstraintViolation;

		public String toString()
		{
			return locationPath;
		}
	}

	static Stream<TestParameter> newTestParameters()
	{
		TestParameter testParameter;
		List<TestParameter> testParameters = new ArrayList<>();

		// valid
		testParameter = new TestParameter();
		testParameter.locationPath = "/codeBook/docDscr/citation/holdings";
		testParameter.expectedConstraintViolation = isEmpty();
		testParameters.add( testParameter );

		// invalid
		testParameter = new TestParameter();
		testParameter.locationPath = "/codeBook/stdyDscr/citation/titlStmt/IDNo[@agency='UKDA']";
		testParameter.expectedConstraintViolation = isPresent();
		testParameters.add( testParameter );

		return testParameters.stream();
	}

	@ParameterizedTest
	@MethodSource( newTestParameters )
	void newValidators( TestParameter testParameter )
	{
		// given
		Constraint constraint = new PredicatelessXPathConstraint( testParameter.locationPath );

		// when
		List<Validator> validators = constraint.newValidators( mockDocument( testParameter.locationPath ) );

		// then
		assertThat( validators, hasSize( 1 ) );
		Validator validator = validators.get( 0 );
		assertThat( validator.validate(), testParameter.expectedConstraintViolation );
	}

	private Document mockDocument( String locationPath )
	{
		Document document = mock( Document.class );
		Node node = new NodeImpl( "/DDIProfile/Used/@xpath", locationPath, null );
		when( document.getNodes( locationPath ) ).thenReturn( Collections.singletonList( node ) );
		return document;
	}
}
