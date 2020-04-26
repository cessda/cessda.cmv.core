package eu.cessda.cmv.core;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import org.gesis.commons.xml.XercesXalanDocument;
import org.gesis.commons.xml.ddi.DdiInputStream;

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
		for ( org.w3c.dom.Node node : document.selectNodes( "/DDIProfile/Used" ) )
		{
			String locationPath = node.getAttributes().getNamedItem( "xpath" ).getNodeValue();
			constraints.add( new CompilableXPathConstraint( locationPath ) );
			constraints.add( new PredicatelessXPathConstraint( locationPath ) );
			if ( node.getAttributes().getNamedItem( "isRequired" ).getNodeValue().equalsIgnoreCase( "true" ) )
			{
				constraints.add( new MandatoryNodeConstraint( locationPath ) );
			}
			else
			{
				constraints.add( new RecommendedNodeConstraint( locationPath ) );
			}
		}
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Constraint> List<T> getConstraints()
	{
		return unmodifiableList( (List<T>) constraints );
	}
}
