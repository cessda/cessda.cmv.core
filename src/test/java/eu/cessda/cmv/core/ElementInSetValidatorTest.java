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
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ElementInSetValidatorTest
{
	@Test
	void validate()
	{
		Set<String> elementSet = new HashSet<>( Arrays.asList( "a", "B", "c", "C" ) );
		assertThat( elementSet, hasSize( 4 ) );
		assertThat( new ElementInSetValidator( mockNode( "a" ), elementSet ).validate(), isEmpty() );
		assertThat( new ElementInSetValidator( mockNode( "A" ), elementSet ).validate(), isPresent() );
		assertThat( new ElementInSetValidator( mockNode( "B" ), elementSet ).validate(), isEmpty() );
		assertThat( new ElementInSetValidator( mockNode( "b" ), elementSet ).validate(), isPresent() );
		assertThat( new ElementInSetValidator( mockNode( "c" ), elementSet ).validate(), isEmpty() );
		assertThat( new ElementInSetValidator( mockNode( "C" ), elementSet ).validate(), isEmpty() );
	}

	private Node mockNode( String element )
	{
		Node node = Mockito.mock( Node.class );
		when( node.getTextContent() ).thenReturn( element );
		return node;
	}
}
