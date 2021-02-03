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
package eu.cessda.cmv.core.controlledvocabulary;

import java.net.URI;
import java.util.Set;

abstract class AbstractControlledVocabularyRepository implements ControlledVocabularyRepository.V11
{
	private URI uri;
	private Set<String> codeValues;
	private Set<String> descriptiveTerms;

	protected void setUri( URI uri )
	{
		this.uri = uri;
	}

	protected void setCodeValues( Set<String> codeValues )
	{
		this.codeValues = codeValues;
	}

	protected void setDescriptiveTerms( Set<String> descriptiveTerms )
	{
		this.descriptiveTerms = descriptiveTerms;
	}

	@Override
	public Set<String> findCodeValues()
	{
		return codeValues;
	}

	@Override
	public Set<String> findDescriptiveTerms()
	{
		return descriptiveTerms;
	}

	@Override
	public URI getUri()
	{
		return uri;
	}
}
