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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class RecommendedNodeConstraint extends NodeConstraint
{
	public RecommendedNodeConstraint( String locationPath )
	{
		super( locationPath );
	}

	@Override
	public List<Validator> newValidators( Document document )
	{
		List<Node> nodes = document.getNodes( getLocationPath() );
		List<Validator> validators = new ArrayList<>();
		validators.add( new RecommendedNodeValidator( getLocationPath(), nodes.size() ) );
		validators.addAll( nodes.stream().map( NotBlankNodeValidator::new ).collect( Collectors.toList() ) );
		return validators;
	}
}
