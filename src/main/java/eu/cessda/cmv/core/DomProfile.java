package eu.cessda.cmv.core;

import static java.util.Collections.unmodifiableList;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.gesis.commons.xml.XercesXalanDocument;
import org.gesis.commons.xml.ddi.DdiInputStream;

import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbDdiProfileContraintsV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbConstraintV0;

public class DomProfile implements Profile.V10
{
	private org.gesis.commons.xml.DomDocument.V11 document;
	private List<Constraint> constraints;

	public DomProfile( DdiInputStream inputStream )
	{
		document = XercesXalanDocument.newBuilder()
				.ofInputStream( inputStream )
				.build();
		constraints = new ArrayList<>();
		for ( org.w3c.dom.Node usedNode : document.selectNodes( "/DDIProfile/Used" ) )
		{
			String locationPath = usedNode.getAttributes().getNamedItem( "xpath" ).getNodeValue();
			constraints.add( new CompilableXPathConstraint( locationPath ) );
			constraints.add( new PredicatelessXPathConstraint( locationPath ) );

			JaxbDdiProfileContraintsV0 root = getRoot( usedNode );
			if ( usedNode.getAttributes().getNamedItem( "isRequired" ).getNodeValue().equalsIgnoreCase( "true" ) )
			{
				constraints.add( new MandatoryNodeConstraint( locationPath ) );
			}
			else
			{
				try
				{
					String canonicalName = root.getConstraints().stream()
							.map( JaxbConstraintV0::getType )
							.filter( type -> type.equals( RecommendedNodeConstraint.class.getCanonicalName() ) )
							.findAny()
							.orElse( OptionalNodeConstraint.class.getCanonicalName() );
					Class<?> clazz = Class.forName( canonicalName );
					Constructor constructor = clazz.getConstructor( String.class );
					constraints.add( (Constraint) constructor.newInstance( locationPath ) );
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
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
