package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.gesis.commons.xml.DomDocument;
import org.w3c.dom.Node;

class CompilableXPathConstraint implements Constraint.V10
{
	private DomDocument.V10 profileDocument;
	private XPathFactory xPathFactory;

	public CompilableXPathConstraint( DomDocument.V10 profileDocument )
	{
		requireNonNull( profileDocument );
		this.profileDocument = profileDocument;
		xPathFactory = XPathFactory.newInstance();
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
		requireNonNull( xPath );
		try
		{
			xPathFactory.newXPath().compile( xPath );
			return Optional.empty();
		}
		catch (XPathExpressionException e)
		{
			String reason = e.getMessage().replace( "javax.xml.transform.TransformerException: ", "" );
			return Optional.of( new CompilableXPathConstraintViolation( xPath, reason ) );
		}
	}
}
