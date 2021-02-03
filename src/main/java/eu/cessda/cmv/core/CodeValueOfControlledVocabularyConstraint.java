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

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;

class CodeValueOfControlledVocabularyConstraint implements Constraint.V20
{
	private String locationPath;

	public CodeValueOfControlledVocabularyConstraint( String locationPath )
	{
		requireNonNull( locationPath );
		this.locationPath = locationPath;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document document )
	{
		requireNonNull( document );
		return (List<T>) ((Document.V10) document).getNodes( locationPath ).stream()
				.map( ControlledVocabularyNode.class::cast )
				.map( CodeValueOfControlledVocabularyValidator::new )
				.collect( Collectors.toList() );
	}
}
