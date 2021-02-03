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

import static java.util.stream.Collectors.toList;
import static org.gesis.commons.xml.xpath.XPathTokenizer.PARENT;

import java.util.ArrayList;
import java.util.List;

import org.gesis.commons.xml.xpath.XPathTokenizer;

class MandatoryNodeIfParentPresentConstraint extends NodeConstraint
{
	public MandatoryNodeIfParentPresentConstraint( String locationPath )
	{
		super( locationPath );
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document document )
	{
		List<Validator> validators = new ArrayList<>();
		List<Node> nodes = ((Document.V11) document).getNodes( getLocationPath() );
		validators.addAll( nodes.stream().map( NotBlankNodeValidator::new ).collect( toList() ) );
		XPathTokenizer tokenizer = new XPathTokenizer( getLocationPath() );
		nodes = ((Document.V11) document).getNodes( tokenizer.getLocationPath( PARENT ) );
		for ( Node node : nodes )
		{
			validators.add( new MandatoryNodeIfParentPresentValidator( tokenizer, node ) );
		}
		return (List<T>) validators;
	}
}
