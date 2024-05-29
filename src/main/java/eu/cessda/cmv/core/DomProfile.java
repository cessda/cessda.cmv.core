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
import java.util.LinkedHashSet;

class DomProfile extends AbstractProfile
{
	public DomProfile( InputStream inputStream )
	{
		this(eu.cessda.cmv.core.mediatype.profile.Profile.read( inputStream ));
	}

	private DomProfile(eu.cessda.cmv.core.mediatype.profile.Profile profile) {
		super(
			profile.getName() != null ? profile.getName().trim() : null,
			profile.getVersion() != null ? profile.getVersion().trim() : null,
			new LinkedHashSet<>(profile.getConstraints().size()),
			new CMVNamespaceContext(profile.getPrefixMap())
		);

		for ( eu.cessda.cmv.core.mediatype.profile.Constraint constraint : profile.getConstraints() )
		{
			if ( constraint instanceof eu.cessda.cmv.core.mediatype.profile.CompilableXPathConstraint )
			{
				parse( (eu.cessda.cmv.core.mediatype.profile.CompilableXPathConstraint) constraint );
			}
			else if ( constraint instanceof eu.cessda.cmv.core.mediatype.profile.FixedValueNodeConstraint )
			{
				parse( (eu.cessda.cmv.core.mediatype.profile.FixedValueNodeConstraint) constraint );
			}
			else if ( constraint instanceof eu.cessda.cmv.core.mediatype.profile.MandatoryNodeConstraint )
			{
				parse( (eu.cessda.cmv.core.mediatype.profile.MandatoryNodeConstraint) constraint );
			}
			else if ( constraint instanceof eu.cessda.cmv.core.mediatype.profile.MandatoryNodeIfParentPresentConstraint )
			{
				parse( (eu.cessda.cmv.core.mediatype.profile.MandatoryNodeIfParentPresentConstraint) constraint);
			}
			else if ( constraint instanceof eu.cessda.cmv.core.mediatype.profile.MaximumElementOccurrenceConstraint )
			{
				parse( (eu.cessda.cmv.core.mediatype.profile.MaximumElementOccurrenceConstraint) constraint );
			}
			else if ( constraint instanceof eu.cessda.cmv.core.mediatype.profile.NotBlankNodeConstraint )
			{
				parse( (eu.cessda.cmv.core.mediatype.profile.NotBlankNodeConstraint) constraint );
			}
			else if ( constraint instanceof eu.cessda.cmv.core.mediatype.profile.OptionalNodeConstraint )
			{
				parse( (eu.cessda.cmv.core.mediatype.profile.OptionalNodeConstraint) constraint );
			}
			else if ( constraint instanceof eu.cessda.cmv.core.mediatype.profile.PredicatelessXPathConstraint )
			{
				parse( (eu.cessda.cmv.core.mediatype.profile.PredicatelessXPathConstraint) constraint );
			}
			else if ( constraint instanceof eu.cessda.cmv.core.mediatype.profile.RecommendedNodeConstraint )
			{
				parse( (eu.cessda.cmv.core.mediatype.profile.RecommendedNodeConstraint) constraint );
			}
			else
			{
				// Ideally this should never be reached, but it is useful in testing to catch any unexpected constraints
				throw new IllegalStateException( "Unexpected constraint " + constraint.getClass() );
			}
		}
	}

	private void parse( eu.cessda.cmv.core.mediatype.profile.MaximumElementOccurrenceConstraint jaxbConstraint )
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

	private void parse( eu.cessda.cmv.core.mediatype.profile.FixedValueNodeConstraint jaxbConstraint )
	{
		FixedValueNodeConstraint constraint = new FixedValueNodeConstraint( jaxbConstraint.getLocationPath(), jaxbConstraint.getFixedValue() );
		constraints.add( constraint );
	}

	private void parse( eu.cessda.cmv.core.mediatype.profile.MandatoryNodeIfParentPresentConstraint jaxbConstraint )
	{
		MandatoryNodeIfParentPresentConstraint constraint = new MandatoryNodeIfParentPresentConstraint( jaxbConstraint.getLocationPath() );
		constraints.add( constraint );
	}
}
