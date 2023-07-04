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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class DomProfile implements Profile
{
	private final List<Constraint> constraints;

	public DomProfile( InputStream inputStream )
	{
		constraints = new ArrayList<>();

		eu.cessda.cmv.core.mediatype.profile.Profile profile = eu.cessda.cmv.core.mediatype.profile.Profile.read( inputStream );
		for ( eu.cessda.cmv.core.mediatype.profile.Constraint constraint : profile.getConstraints() )
		{
			if ( constraint instanceof eu.cessda.cmv.core.mediatype.profile.CompilableXPathConstraint )
			{
				parse( (eu.cessda.cmv.core.mediatype.profile.CompilableXPathConstraint) constraint );
			}
			else if ( constraint instanceof eu.cessda.cmv.core.mediatype.profile.PredicatelessXPathConstraint )
			{
				parse( (eu.cessda.cmv.core.mediatype.profile.PredicatelessXPathConstraint) constraint );
			}
			else if ( constraint instanceof eu.cessda.cmv.core.mediatype.profile.MandatoryNodeConstraint )
			{
				parse( (eu.cessda.cmv.core.mediatype.profile.MandatoryNodeConstraint) constraint );
			}
			else if ( constraint instanceof eu.cessda.cmv.core.mediatype.profile.RecommendedNodeConstraint )
			{
				parse( (eu.cessda.cmv.core.mediatype.profile.RecommendedNodeConstraint) constraint );
			}
			else if ( constraint instanceof eu.cessda.cmv.core.mediatype.profile.OptionalNodeConstraint )
			{
				parse( (eu.cessda.cmv.core.mediatype.profile.OptionalNodeConstraint) constraint );
			}
			else if ( constraint instanceof eu.cessda.cmv.core.mediatype.profile.MaximumElementOccuranceConstraint )
			{
				parse( (eu.cessda.cmv.core.mediatype.profile.MaximumElementOccuranceConstraint) constraint );
			}
			else if ( constraint instanceof eu.cessda.cmv.core.mediatype.profile.NotBlankNodeConstraint )
			{
				parse( (eu.cessda.cmv.core.mediatype.profile.NotBlankNodeConstraint) constraint );
			}

		}
	}

	@Override
	public List<Constraint> getConstraints()
	{
		return constraints;
	}

	private void parse( eu.cessda.cmv.core.mediatype.profile.MaximumElementOccuranceConstraint jaxbConstraint )
	{
		MaximumElementOccurrenceConstraint constraint = new MaximumElementOccurrenceConstraint(
				jaxbConstraint.getLocationPath(),
				jaxbConstraint.getMaxOccurs() );
		constraints.add( constraint );
	}

	private void parse( eu.cessda.cmv.core.mediatype.profile.OptionalNodeConstraint jaxbConstraint )
	{
		OptionalNodeConstraint constraint = new OptionalNodeConstraint( jaxbConstraint.getLocationPath() );
		constraints.add( constraint );
	}

	private void parse( eu.cessda.cmv.core.mediatype.profile.NotBlankNodeConstraint jaxbConstraint )
	{
		NotBlankNodeConstraint constraint = new NotBlankNodeConstraint( jaxbConstraint.getLocationPath() );
		constraints.add( constraint );
	}

	private void parse( eu.cessda.cmv.core.mediatype.profile.RecommendedNodeConstraint jaxbConstraint )
	{
		RecommendedNodeConstraint constraint = new RecommendedNodeConstraint( jaxbConstraint.getLocationPath() );
		constraints.add( constraint );
	}

	private void parse( eu.cessda.cmv.core.mediatype.profile.MandatoryNodeConstraint jaxbConstraint )
	{
		MandatoryNodeConstraint constraint = new MandatoryNodeConstraint( jaxbConstraint.getLocationPath() );
		constraints.add( constraint );
	}

	private void parse( eu.cessda.cmv.core.mediatype.profile.PredicatelessXPathConstraint jaxbConstraint )
	{
		PredicatelessXPathConstraint constraint = new PredicatelessXPathConstraint( jaxbConstraint.getLocationPath() );
		constraints.add( constraint );
	}

	private void parse( eu.cessda.cmv.core.mediatype.profile.CompilableXPathConstraint jaxbConstraint )
	{
		CompilableXPathConstraint constraint = new CompilableXPathConstraint( jaxbConstraint.getLocationPath() );
		constraints.add( constraint );
	}
}
