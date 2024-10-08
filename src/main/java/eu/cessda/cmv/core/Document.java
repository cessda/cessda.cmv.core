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

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepository;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPathExpressionException;
import java.net.URI;
import java.util.List;

public interface Document
{
	/**
	 * Gets the URI of the source of the document.
	 *
	 * @return the URI, or {@code null} if the document was constructed from a source that didn't specify a URI.
	 */
	URI getURI();

	/**
	 * Get a list of DOM nodes found at the given XPath.
	 *
	 * @param locationPath the XPath to the nodes to look up.
	 */
	List<Node> getNodes( String locationPath ) throws XPathExpressionException;

	/**
	 * Register a {@link ControlledVocabularyRepository} instance. The {@link ControlledVocabularyRepository}
	 * can be looked up using {@link #findControlledVocabularyRepository(URI)} with a registered URI.
	 *
	 * @param controlledVocabularyRepository the repository.
	 */
	void register( ControlledVocabularyRepository controlledVocabularyRepository );

	/**
	 * Find a registered {@link ControlledVocabularyRepository} instance.
	 * <p>
	 * {@link ControlledVocabularyRepository} instances are registered using
	 * {@link #register(ControlledVocabularyRepository)}.
	 *
	 * @param uri the URI of the controlled vocabulary.
	 * @return the controlled vocabulary, or {@code null} if the uri was not registered.
	 */
	ControlledVocabularyRepository findControlledVocabularyRepository( URI uri );

	void setNamespaceContext( NamespaceContext namespaceContext );
}
