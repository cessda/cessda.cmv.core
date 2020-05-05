package eu.cessda.cmv.core;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.gesis.commons.xml.XercesXalanDocument;
import org.gesis.commons.xml.ddi.DdiInputStream;

import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbDdiProfileContraintsV0;

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
			String locationPath = usedNode.getAttributes().getNamedItem( "xpath" ).getNodeValue();
			constraints.add( new CompilableXPathConstraint( locationPath ) );
			constraints.add( new PredicatelessXPathConstraint( locationPath ) );

			JaxbDdiProfileContraintsV0 instructions = getRoot( usedNode );
			if ( usedNode.getAttributes().getNamedItem( "isRequired" ).getNodeValue().equalsIgnoreCase( "true" ) )
			{
				constraints.add( new MandatoryNodeConstraint( locationPath ) );
			}
			else
			{
				String canonicalName = instructions.getConstraints().stream()
						.map( JaxbConstraintV0::getType )
						.filter( type -> type.equals( RecommendedNodeConstraint.class.getCanonicalName() ) )
						.findAny()
						.orElse( OptionalNodeConstraint.class.getCanonicalName() );
				constraints.add( newConstraint( canonicalName, locationPath ) );
			}

			List<String> canonicalNames = constraints.stream()
					.map( c -> c.getClass().getCanonicalName() )
					.collect( Collectors.toList() );
			constraints.addAll( instructions.getConstraints().stream()
					.map( JaxbConstraintV0::getType )
					.filter( type -> !canonicalNames.contains( type ) )
					.map( canonicalName -> newConstraint( canonicalName, locationPath ) )
					.collect( toList() ) );
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

	private JaxbDdiProfileContraintsV0 getRoot( org.w3c.dom.Node usedNode )
	{
		org.w3c.dom.Node cmvNode = document.selectNode( usedNode, "Instructions/Content" );
		if ( cmvNode != null )
		{
			return JaxbDdiProfileContraintsV0.fromString( cmvNode.getTextContent() );
		}
		else
		{
			return new JaxbDdiProfileContraintsV0();
		}
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Constraint> List<T> getConstraints()
	{
		return unmodifiableList( (List<T>) constraints );
	}
}
