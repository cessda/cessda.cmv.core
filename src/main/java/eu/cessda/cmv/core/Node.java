/*-
 * #%L
 * cmv-core
 * %%
 * Copyright (C) 2020 - 2023 CESSDA ERIC
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

import org.gesis.commons.xml.LocationInfo;

public interface Node
{
	/**
	 * The XPath of this node in the document.
	 */
	String getLocationPath();

	/**
	 * The text content of this node and its descendants.
	 */
	String getTextContent();

	/**
	 * The location of this node in the document.
	 */
	LocationInfo getLocationInfo();

	/**
	 * Returns the number of children of this node that satisfy the given XPath.
	 * <p>
	 * The XPath is resolved relative to this node. For example, a node at
	 * {@code /node} and a relative location path of {@code ./child} would
	 * be resolved to {@code /node/child}.
	 *
	 * @return the count of children that satisfy the given relative XPath.
	 */
	int getChildCount( String relativeLocationPath );
}
