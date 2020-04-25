package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

class CompilableXPathValidator implements Validator.V10
{
	private XPathFactory factory;
	private String reason;
	private Node node;

	CompilableXPathValidator( Node node )
	{
		this( node, XPathFactory.newInstance() );
	}

	CompilableXPathValidator( Node node, XPathFactory factory )
	{
		requireNonNull( node );
		requireNonNull( factory );

		this.node = node;
		this.factory = factory;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		try
		{
			factory.newXPath().compile( node.getTextContent() );
			return empty();
		}
		catch (XPathExpressionException e)
		{
			reason = e.getMessage().replace( "javax.xml.transform.TransformerException: ", "" );
			return of( newConstraintViolation() );
		}
	}

	private ConstraintViolation newConstraintViolation()
	{
		String message = "'%s' is not a compilable XPath: %s";
		message = String.format( message, node.getTextContent(), reason );
		return new ConstraintViolation( message, node.getLocationInfo() );
	}
}
