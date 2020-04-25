package eu.cessda.cmv.core;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.util.Optional;

import org.gesis.commons.xml.xpath.XPathTokenizer;

class PredicatelessXPathValidator implements Validator.V10
{
	private String locationPath;

	PredicatelessXPathValidator( String locationPath )
	{
		this.locationPath = locationPath;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		XPathTokenizer tokenizer = new XPathTokenizer( locationPath );
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
		message = String.format( message, locationPath );
		return new ConstraintViolation( message, empty() );
	}
}
