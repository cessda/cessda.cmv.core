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

import org.gesis.commons.resource.io.DdiInputStream;
import org.gesis.commons.xml.DomDocument;
import org.gesis.commons.xml.XercesXalanDocument;
import org.w3c.dom.Attr;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;

class DomSemiStructuredDdiProfile implements Profile
{
	private final org.gesis.commons.xml.DomDocument.V11 document;
	private List<Constraint> constraints;
	private final eu.cessda.cmv.core.mediatype.profile.Profile jaxbProfile;

	public DomSemiStructuredDdiProfile( DdiInputStream inputStream )
	{
		document = XercesXalanDocument.newBuilder().ofInputStream( inputStream ).build();

		jaxbProfile = new eu.cessda.cmv.core.mediatype.profile.Profile();
		parseProfileName();

		constraints = new ArrayList<>();
		for ( org.w3c.dom.Node usedNode : document.selectNodes( "/DDIProfile/Used" ) )
		{
			constraints.add( new CompilableXPathConstraint( getLocationPath( usedNode ) ) );
			jaxbProfile.getConstraints().add( new eu.cessda.cmv.core.mediatype.profile.CompilableXPathConstraint( getLocationPath( usedNode ) ) );
			constraints.add( new PredicatelessXPathConstraint( getLocationPath( usedNode ) ) );
			jaxbProfile.getConstraints().add( new eu.cessda.cmv.core.mediatype.profile.PredicatelessXPathConstraint( getLocationPath( usedNode ) ) );
			parseControlledVocabularyRepositoryConstraint( usedNode );

			parseMandatoryNodeConstraint( usedNode );
			parseOptionalNodeConstraint( usedNode );
			parseNotBlankNodeConstraint( usedNode );
			parseMaximumElementOccurrenceConstraint( usedNode );
			parseMandatoryNodeIfParentPresentConstraint( usedNode );
			parseFixedValueNodeConstraint( usedNode );

			parseCodeValueOfControlledVocabularyConstraint( usedNode );
			parseDescriptiveTermOfControlledVocabularyConstraint( usedNode );
		}

		if ( constraints.isEmpty() )
		{
			throw new IllegalArgumentException( "Profile defines no constraints" );
		}

		// Reorder the constraints so that ControlledVocabularyRepositoryConstraints are first
		ArrayDeque<Constraint> orderedConstraints = new ArrayDeque<>( constraints.size() );

		for ( Constraint c : constraints )
		{
			if ( c instanceof ControlledVocabularyRepositoryConstraint )
			{
				orderedConstraints.addFirst( c );
			}
			else
			{
				orderedConstraints.addLast( c );
			}
		}

		this.constraints = new ArrayList<>(orderedConstraints);
	}

	private static String getLocationPath( org.w3c.dom.Node usedNode )
	{
		return usedNode.getAttributes().getNamedItem( "xpath" ).getTextContent();
	}

	private void parseProfileName()
	{
		org.w3c.dom.Node nameNode = document.selectNode( "/DDIProfile/DDIProfileName" );
		if ( nameNode != null )
		{
			jaxbProfile.setName( nameNode.getTextContent().trim() );
		}
	}

	private Optional<DomDocument.V13> findExtension( org.w3c.dom.Node usedNode )
	{
		String extensionRecognizingXPath = "Instructions/Content[contains(.,'<Constraints>')]";
		org.w3c.dom.Node constraintsNode = document.selectNode( usedNode, extensionRecognizingXPath );
		if ( constraintsNode != null )
		{
			return Optional.of( (DomDocument.V13) XercesXalanDocument.newBuilder()
					.ofContent( constraintsNode.getTextContent().trim() )
					.printPrettyWithIndentation( 2 )
					.build()
					.omitWhitespaceOnlyTextNodes() );
		}
		else
		{
			return Optional.empty();
		}
	}

	private void parseMandatoryNodeConstraint( org.w3c.dom.Node usedNode )
	{
		org.w3c.dom.Node isRequiredNode = usedNode.getAttributes().getNamedItem( "isRequired" );
		if ( isRequiredNode != null && isRequiredNode.getNodeValue().equalsIgnoreCase( "true" ) )
		{
			constraints.add( new MandatoryNodeConstraint( getLocationPath( usedNode ) ) );
			jaxbProfile.getConstraints().add( new eu.cessda.cmv.core.mediatype.profile.MandatoryNodeConstraint( getLocationPath( usedNode ) ) );
		}
	}

	private void parseFixedValueNodeConstraint( org.w3c.dom.Node usedNode )
	{
		org.w3c.dom.Node fixedValueNode = usedNode.getAttributes().getNamedItem( "fixedValue" );
		if ( fixedValueNode != null && fixedValueNode.getNodeValue().equalsIgnoreCase( "true" ) )
		{
			org.w3c.dom.Node defaultValueNode = usedNode.getAttributes().getNamedItem( "defaultValue" );
			// TODO Find similar or generalized constraint like
			// MandatoryNodeIfParentPresentConstraint
			// defaultValue attribute is mandatory if fixedValue attribute is present
			if ( defaultValueNode != null )
			{
				String fixedValue = defaultValueNode.getTextContent().trim();
				Constraint constraint = new FixedValueNodeConstraint( getLocationPath( usedNode ), fixedValue );
				constraints.add( constraint );
				// TODO jaxbProfile.getConstraints().add( ... ) );
			}
		}
	}

	private void parseOptionalNodeConstraint( org.w3c.dom.Node usedNode )
	{
		org.w3c.dom.Node isRequiredNode = usedNode.getAttributes().getNamedItem( "isRequired" );

		if ( isRequiredNode != null && "true".equalsIgnoreCase( ((Attr) isRequiredNode).getValue() ) )
		{
			return;
		}

		if ( document.selectNode( usedNode, "Description[Content='Required: Recommended']" ) != null
				|| document.selectNode( usedNode, "Description[Content='Recommended']" ) != null
				|| hasRecommendedNodeConstraintExtension( usedNode ) )
		{
			constraints.add( new RecommendedNodeConstraint( getLocationPath( usedNode ) ) );
			jaxbProfile.getConstraints().add( new eu.cessda.cmv.core.mediatype.profile.RecommendedNodeConstraint( getLocationPath( usedNode ) ) );
		}
		else
		{
			constraints.add( new OptionalNodeConstraint( getLocationPath( usedNode ) ) );
			jaxbProfile.getConstraints().add( new eu.cessda.cmv.core.mediatype.profile.OptionalNodeConstraint( getLocationPath( usedNode ) ) );
		}
	}

	@SuppressWarnings( "squid:S1075" )
	private boolean hasRecommendedNodeConstraintExtension( org.w3c.dom.Node usedNode )
	{
		Optional<DomDocument.V13> extension = findExtension( usedNode );
		if ( extension.isPresent() )
		{
			String locationPath = "/Constraints/RecommendedNodeConstraint";
			return !extension.get().selectNodes( locationPath ).isEmpty();
		}
		return false;
	}

	@SuppressWarnings( "squid:S1075" )
	private void parseCodeValueOfControlledVocabularyConstraint( org.w3c.dom.Node usedNode )
	{
		findExtension( usedNode ).ifPresent( extension ->
		{
			String locationPath = "/Constraints/CodeValueOfControlledVocabularyConstraint";
			int nodeCount = extension.selectNodes( locationPath ).size();
			for ( int i = 0; i < nodeCount; i++ )
			{
				constraints.add( new CodeValueOfControlledVocabularyConstraint( getLocationPath( usedNode ) ) );
			}
		} );
	}

	@SuppressWarnings( "squid:S1075" )
	private void parseDescriptiveTermOfControlledVocabularyConstraint( org.w3c.dom.Node usedNode )
	{
		findExtension( usedNode ).ifPresent( extension ->
		{
			String locationPath = "/Constraints/DescriptiveTermOfControlledVocabularyConstraint";
			int nodeCount = extension.selectNodes( locationPath ).size();
			for ( int i = 0; i < nodeCount; i++ )
			{
				constraints.add( new DescriptiveTermOfControlledVocabularyConstraint( getLocationPath( usedNode ) ) );
			}
		} );
	}

	@SuppressWarnings( "squid:S1075" )
	private void parseControlledVocabularyRepositoryConstraint( org.w3c.dom.Node usedNode )
	{
		findExtension( usedNode ).ifPresent( extension ->
		{
			String locationPath = "/Constraints/ControlledVocabularyRepositoryConstraint";
			for ( org.w3c.dom.Node constraintNode : extension.selectNodes( locationPath ) )
			{
				Constraint constraint = new ControlledVocabularyRepositoryConstraint(
						getLocationPath( usedNode ),
						extension.selectNode( constraintNode, "./RepositoryType" ).getTextContent(),
						extension.selectNode( constraintNode, "./RepositoryUri" ).getTextContent() );
				constraints.add( constraint );
			}
		} );
	}

	private void parseMaximumElementOccurrenceConstraint( org.w3c.dom.Node usedNode )
	{
		org.w3c.dom.Node limitMaxOccursNode = usedNode.getAttributes().getNamedItem( "limitMaxOccurs" );
		if ( limitMaxOccursNode != null )
		{
			constraints.add( new MaximumElementOccurrenceConstraint(
					getLocationPath( usedNode ),
					Long.parseLong( limitMaxOccursNode.getNodeValue() ) ) );
			jaxbProfile.getConstraints().add( new eu.cessda.cmv.core.mediatype.profile.MaximumElementOccurrenceConstraint(
					getLocationPath( usedNode ),
					Long.parseLong( limitMaxOccursNode.getNodeValue() ) ) );
		}
	}

	@SuppressWarnings( "squid:S1075" )
	private void parseMandatoryNodeIfParentPresentConstraint( org.w3c.dom.Node usedNode )
	{
		findExtension( usedNode ).ifPresent( extension ->
		{
			String locationPath = "/Constraints/MandatoryNodeIfParentPresentConstraint";
			if ( !extension.selectNodes( locationPath ).isEmpty() )
			{
				Constraint constraint = new MandatoryNodeIfParentPresentConstraint( getLocationPath( usedNode ) );
				constraints.add( constraint );
			}
		} );
	}

	@SuppressWarnings( "squid:S1075" )
	private void parseNotBlankNodeConstraint( org.w3c.dom.Node usedNode )
	{
		findExtension( usedNode ).ifPresent( extension ->
		{
			String locationPath = "/Constraints/NotBlankNodeConstraint";
			if ( !extension.selectNodes( locationPath ).isEmpty() )
			{
				Constraint constraint = new NotBlankNodeConstraint( getLocationPath( usedNode ) );
				constraints.add( constraint );
			}
		} );
	}

	@Override
	public List<Constraint> getConstraints()
	{
		return unmodifiableList( constraints );
	}

	public eu.cessda.cmv.core.mediatype.profile.Profile toJaxbProfileV0()
	{
		return jaxbProfile;
	}
}
