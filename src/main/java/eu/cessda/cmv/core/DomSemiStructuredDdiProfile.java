package eu.cessda.cmv.core;

import static java.lang.Long.valueOf;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.gesis.commons.xml.DomDocument;
import org.gesis.commons.xml.XercesXalanDocument;
import org.gesis.commons.xml.ddi.DdiInputStream;

import eu.cessda.cmv.core.mediatype.profile.v0.CompilableXPathConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.MandatoryNodeConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.MaximumElementOccuranceConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.OptionalNodeConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.PredicatelessXPathConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.ProfileV0;
import eu.cessda.cmv.core.mediatype.profile.v0.RecommendedNodeConstraintV0;

class DomSemiStructuredDdiProfile implements Profile.V10
{
	private org.gesis.commons.xml.DomDocument.V11 document;
	private List<Constraint> constraints;
	private ProfileV0 jaxbProfile;

	public DomSemiStructuredDdiProfile( DdiInputStream inputStream )
	{
		document = XercesXalanDocument.newBuilder().ofInputStream( inputStream ).build();

		jaxbProfile = new ProfileV0();
		parseProfileName();

		constraints = new ArrayList<>();
		for ( org.w3c.dom.Node usedNode : document.selectNodes( "/DDIProfile/Used" ) )
		{
			constraints.add( new CompilableXPathConstraint( getLocationPath( usedNode ) ) );
			jaxbProfile.getConstraints().add( new CompilableXPathConstraintV0( getLocationPath( usedNode ) ) );
			constraints.add( new PredicatelessXPathConstraint( getLocationPath( usedNode ) ) );
			jaxbProfile.getConstraints().add( new PredicatelessXPathConstraintV0( getLocationPath( usedNode ) ) );
			parseControlledVocabularyRepositoryConstraint( usedNode );

			parseMandatoryNodeConstraint( usedNode );
			parseOptionalNodeConstraint( usedNode );
			parseMaximumElementOccuranceConstraint( usedNode );
			parseMandatoryNodeIfParentPresentConstraint( usedNode );
			parseFixedValueNodeConstraint( usedNode );

			parseCodeValueOfControlledVocabularyConstraint( usedNode );
			parseDescriptiveTermOfControlledVocabularyConstraint( usedNode );
		}
		constraints = forceConstraintReordering( constraints );
	}

	private List<Constraint> forceConstraintReordering( List<Constraint> constraints )
	{
		LinkedList<Constraint> list = new LinkedList<>();
		for ( Constraint c : constraints )
		{
			if ( c instanceof ControlledVocabularyRepositoryConstraint )
			{
				list.addFirst( c );
			}
			else
			{
				list.add( c );
			}
		}
		return list;
	}

	private String getLocationPath( org.w3c.dom.Node usedNode )
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
			jaxbProfile.getConstraints().add( new MandatoryNodeConstraintV0( getLocationPath( usedNode ) ) );
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
		if ( isRequiredNode == null || isRequiredNode.getNodeValue().equalsIgnoreCase( "false" ) )
		{
			if ( document.selectNode( usedNode, "Description[Content='Required: Recommended']" ) != null
					|| document.selectNode( usedNode, "Description[Content='Recommended']" ) != null
					|| hasRecommendedNodeConstraintExtension( usedNode ) )
			{
				constraints.add( new RecommendedNodeConstraint( getLocationPath( usedNode ) ) );
				jaxbProfile.getConstraints().add( new RecommendedNodeConstraintV0( getLocationPath( usedNode ) ) );
			}
			else
			{
				constraints.add( new OptionalNodeConstraint( getLocationPath( usedNode ) ) );
				jaxbProfile.getConstraints().add( new OptionalNodeConstraintV0( getLocationPath( usedNode ) ) );
			}
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
			for ( org.w3c.dom.Node constraintNode : extension.selectNodes( locationPath ) )
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
			for ( org.w3c.dom.Node constraintNode : extension.selectNodes( locationPath ) )
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

	private void parseMaximumElementOccuranceConstraint( org.w3c.dom.Node usedNode )
	{
		org.w3c.dom.Node limitMaxOccursNode = usedNode.getAttributes().getNamedItem( "limitMaxOccurs" );
		if ( limitMaxOccursNode != null )
		{
			constraints.add( new MaximumElementOccuranceConstraint(
					getLocationPath( usedNode ),
					valueOf( limitMaxOccursNode.getNodeValue() ) ) );
			jaxbProfile.getConstraints().add( new MaximumElementOccuranceConstraintV0(
					getLocationPath( usedNode ),
					valueOf( limitMaxOccursNode.getNodeValue() ) ) );
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

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Constraint> List<T> getConstraints()
	{
		return unmodifiableList( (List<T>) constraints );
	}

	public ProfileV0 toJaxbProfileV0()
	{
		return jaxbProfile;
	}
}
