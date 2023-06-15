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

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepositoryProxy;

import java.util.ArrayList;
import java.util.List;

class ControlledVocabularyRepositoryConstraint implements Constraint.V20
{
	private final String locationPath;
	private final String type;
	private final String uri;

	public ControlledVocabularyRepositoryConstraint(
			String locationPath,
			String repositoryType,
			String repositoryUri )
	{
		this.locationPath = locationPath;
		this.type = repositoryType;
		this.uri = repositoryUri;
	}

	@Override
	public List<Validator> newValidators( Document document )
	{
		Document.V11 d = (Document.V11) document;
		ControlledVocabularyRepositoryProxy proxy = new ControlledVocabularyRepositoryProxy( type, uri, true );
		d.register( uri, proxy );
		int count = 0;
		for ( Node node : d.getNodes( locationPath ) )
		{
			if ( node.getTextContent().contentEquals( uri ) )
			{
				count++;
			}
		}
		List<Validator> validators = new ArrayList<>();
		if ( count > 0 )
		{
			validators.add( new ControlledVocabularyRepositoryValidator( proxy ) );
		}
		return validators;
	}
}
