package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.gesis.commons.xml.DefaultXPathTokenizer;
import org.gesis.commons.xml.DomDocument;
import org.w3c.dom.Node;

class PredicatelessXPathConstraint implements Constraint.V10
{
	private DomDocument.V10 profileDocument;

	public PredicatelessXPathConstraint( DomDocument.V10 profileDocument )
	{
		requireNonNull( profileDocument );
		this.profileDocument = profileDocument;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public List<ConstraintViolation.V10> validate()
	{
		return profileDocument.selectNodes( "/DDIProfile/Used/@xpath" ).stream()
				.map( Node::getTextContent )
				.map( this::validate )
				.filter( Optional::isPresent ).map( Optional::get )
				.collect( Collectors.toList() );
	}

	private Optional<ConstraintViolation.V10> validate( String xPath )
	{
		if ( new DefaultXPathTokenizer( xPath ).containsPredicate() )
		{
			return Optional.of( new PredicatelessXPathConstraintViolation( xPath ) );
		}
		else
		{
			return Optional.empty();
		}
	}
}
