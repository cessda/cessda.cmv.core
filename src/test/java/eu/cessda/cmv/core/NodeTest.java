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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class NodeTest
{
	@Test
	void getChildCount()
	{
		NodeImpl node = new NodeImpl( "/path/to/element", "value", null );
		String locationPath = "./@attribute";
		assertThat( node.getChildCount( locationPath ), equalTo( 0 ) );
		node.incrementChildCount( locationPath );
		node.incrementChildCount( locationPath );
		node.incrementChildCount( locationPath );
		assertThat( node.getChildCount( locationPath ), equalTo( 3 ) );
	}
}
