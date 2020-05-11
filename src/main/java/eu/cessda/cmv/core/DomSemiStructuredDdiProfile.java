package eu.cessda.cmv.core;

import static java.lang.Long.valueOf;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import org.gesis.commons.xml.XercesXalanDocument;
import org.gesis.commons.xml.ddi.DdiInputStream;

import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbCompilableXPathConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbMandatoryNodeConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbMaximumElementOccuranceConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbOptionalNodeConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbPredicatelessXPathConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbProfileV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbRecommendedNodeConstraintV0;

public class DomSemiStructuredDdiProfile implements Profile.V10
{
	private org.gesis.commons.xml.DomDocument.V11 document;
	private List<Constraint> constraints;
	private JaxbProfileV0 jaxbProfile;

	public DomSemiStructuredDdiProfile( DdiInputStream inputStream )
	{
		document = XercesXalanDocument.newBuilder().ofInputStream( inputStream ).build();
		constraints = new ArrayList<>();
		jaxbProfile = new JaxbProfileV0();

		parseProfileName();
		for ( org.w3c.dom.Node usedNode : document.selectNodes( "/DDIProfile/Used" ) )
		{
			constraints.add( new CompilableXPathConstraint( getLocationPath( usedNode ) ) );
			jaxbProfile.getConstraints().add( new JaxbCompilableXPathConstraintV0( getLocationPath( usedNode ) ) );
			constraints.add( new PredicatelessXPathConstraint( getLocationPath( usedNode ) ) );
			jaxbProfile.getConstraints().add( new JaxbPredicatelessXPathConstraintV0( getLocationPath( usedNode ) ) );
			parseMandatoryNodeConstraint( usedNode );
			parseOptionalNodeConstraint( usedNode );
			parseMaximumElementOccuranceConstraint( usedNode );
		}
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

	private void parseMandatoryNodeConstraint( org.w3c.dom.Node usedNode )
	{
		org.w3c.dom.Node isRequiredNode = usedNode.getAttributes().getNamedItem( "isRequired" );
		if ( isRequiredNode != null && isRequiredNode.getNodeValue().equalsIgnoreCase( "true" ) )
		{
			constraints.add( new MandatoryNodeConstraint( getLocationPath( usedNode ) ) );
			jaxbProfile.getConstraints().add( new JaxbMandatoryNodeConstraintV0( getLocationPath( usedNode ) ) );
		}
	}

	private void parseOptionalNodeConstraint( org.w3c.dom.Node usedNode )
	{
		org.w3c.dom.Node isRequiredNode = usedNode.getAttributes().getNamedItem( "isRequired" );
		if ( isRequiredNode == null || isRequiredNode.getNodeValue().equalsIgnoreCase( "false" ) )
		{
			if ( document.selectNode( usedNode, "Description[Content='Required: Recommended']" ) != null
					|| document.selectNode( usedNode, "Description[Content='Recommended']" ) != null )
			{
				constraints.add( new RecommendedNodeConstraint( getLocationPath( usedNode ) ) );
				jaxbProfile.getConstraints().add( new JaxbRecommendedNodeConstraintV0( getLocationPath( usedNode ) ) );
			}
			else
			{
				constraints.add( new OptionalNodeConstraint( getLocationPath( usedNode ) ) );
				jaxbProfile.getConstraints().add( new JaxbOptionalNodeConstraintV0( getLocationPath( usedNode ) ) );
			}
		}
	}

	private void parseMaximumElementOccuranceConstraint( org.w3c.dom.Node usedNode )
	{
		org.w3c.dom.Node limitMaxOccursNode = usedNode.getAttributes().getNamedItem( "limitMaxOccurs" );
		if ( limitMaxOccursNode != null )
		{
			constraints.add( new MaximumElementOccuranceConstraint(
					getLocationPath( usedNode ),
					valueOf( limitMaxOccursNode.getNodeValue() ) ) );
			jaxbProfile.getConstraints().add( new JaxbMaximumElementOccuranceConstraintV0(
					getLocationPath( usedNode ),
					valueOf( limitMaxOccursNode.getNodeValue() ) ) );
		}
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Constraint> List<T> getConstraints()
	{
		return unmodifiableList( (List<T>) constraints );
	}

	public JaxbProfileV0 toJaxbProfileV0()
	{
		return jaxbProfile;
	}
}
