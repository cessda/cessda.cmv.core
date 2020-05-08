package eu.cessda.cmv.core;

import static java.lang.Long.valueOf;
import static java.util.Collections.unmodifiableList;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.gesis.commons.xml.XercesXalanDocument;
import org.gesis.commons.xml.ddi.DdiInputStream;

import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbDdiProfileConstraintsV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbRecommendedNodeConstraintV0;

public class DomProfile implements Profile.V10
{
	private org.gesis.commons.xml.DomDocument.V11 document;
	private List<Constraint> constraints;

	public DomProfile( DdiInputStream inputStream )
	{
		document = XercesXalanDocument.newBuilder().ofInputStream( inputStream ).build();
		constraints = new ArrayList<>();
		for ( org.w3c.dom.Node usedNode : document.selectNodes( "/DDIProfile/Used" ) )
		{
			constraints.add( new CompilableXPathConstraint( getLocationPath( usedNode ) ) );
			constraints.add( new PredicatelessXPathConstraint( getLocationPath( usedNode ) ) );

			parseMandatoryNodeConstraint( usedNode );
			parseOptionalNodeConstraint( usedNode );
			parseMaximumElementOccuranceConstraint( usedNode );
		}
	}

	private String getLocationPath( org.w3c.dom.Node usedNode )
	{
		return usedNode.getAttributes().getNamedItem( "xpath" ).getTextContent();
	}

	private void parseMandatoryNodeConstraint( org.w3c.dom.Node usedNode )
	{
		org.w3c.dom.Node isRequiredNode = usedNode.getAttributes().getNamedItem( "isRequired" );
		if ( isRequiredNode != null && isRequiredNode.getNodeValue().equalsIgnoreCase( "true" ) )
		{
			constraints.add( new MandatoryNodeConstraint( getLocationPath( usedNode ) ) );
		}
	}

	private void parseOptionalNodeConstraint( org.w3c.dom.Node usedNode )
	{
		org.w3c.dom.Node isRequiredNode = usedNode.getAttributes().getNamedItem( "isRequired" );
		if ( isRequiredNode == null || isRequiredNode.getNodeValue().equalsIgnoreCase( "false" ) )
		{
			String canonicalName = getDdiProfileConstraints( usedNode ).getConstraints().stream()
					.filter( c -> c instanceof JaxbRecommendedNodeConstraintV0 )
					.map( c -> RecommendedNodeConstraint.class.getCanonicalName() )
					.findAny()
					.orElse( OptionalNodeConstraint.class.getCanonicalName() );
			constraints.add( newConstraint( canonicalName, getLocationPath( usedNode ) ) );
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
		}
	}

	private Constraint newConstraint( String canonicalName, String locationPath )
	{
		try
		{
			@SuppressWarnings( "unchecked" )
			Class<Constraint> clazz = (Class<Constraint>) Class.forName( canonicalName );
			Constructor<Constraint> constructor = clazz.getConstructor( String.class );
			return constructor.newInstance( locationPath );
		}
		catch (Exception e)
		{
			throw new IllegalArgumentException( e );
		}
	}

	private JaxbDdiProfileConstraintsV0 getDdiProfileConstraints( org.w3c.dom.Node usedNode )
	{
		org.w3c.dom.Node cmvNode = document.selectNode( usedNode, "Instructions/Content" );
		if ( cmvNode != null )
		{
			return JaxbDdiProfileConstraintsV0.fromString( cmvNode.getTextContent() );
		}
		else
		{
			return new JaxbDdiProfileConstraintsV0();
		}
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Constraint> List<T> getConstraints()
	{
		return unmodifiableList( (List<T>) constraints );
	}
}
