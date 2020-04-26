package eu.cessda.cmv.core;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.util.Optional;

import org.gesis.commons.xml.xpath.XPathTokenizer;

class PredicatelessXPathValidator implements Validator.V10
{
	private Node node;

	PredicatelessXPathValidator( Node node )
	{
		this.node = node;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		XPathTokenizer tokenizer = new XPathTokenizer( node.getTextContent() );
		if ( tokenizer.containsPredicates() )
		{
			return ofNullable( newConstraintViolation() );
		}
		else
		{
			return empty();
		}
	}

	private ConstraintViolation newConstraintViolation()
	{
		String message = "'%s' contains a predicate";
		message = String.format( message, node.getTextContent() );
		return new ConstraintViolation( message, empty() );
	}
}
