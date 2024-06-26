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
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

abstract class AbstractControlledVocabularyRepository implements ControlledVocabularyRepository
{
	protected AbstractControlledVocabularyRepository( URI uri )
	{
		this.uri = uri;
	}

	private final URI uri;
	private Set<String> codeValues;
	private Set<String> descriptiveTerms;

	protected void setCodeValues( Set<String> codeValues )
	{
		this.codeValues = Collections.unmodifiableSet( codeValues );
	}

	protected void setDescriptiveTerms( Set<String> descriptiveTerms )
	{
		this.descriptiveTerms = Collections.unmodifiableSet( descriptiveTerms );
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

	@Override
	public boolean equals( Object o )
	{
		if ( this == o ) return true;
		if ( o == null || getClass() != o.getClass() ) return false;
		AbstractControlledVocabularyRepository that = (AbstractControlledVocabularyRepository) o;
		return uri.equals( that.uri );
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( uri );
	}
}
