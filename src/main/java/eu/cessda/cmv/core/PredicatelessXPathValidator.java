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
	@SuppressWarnings( "unchecked" )
	public <T extends ConstraintViolation> Optional<T> validate()
	{
		XPathTokenizer tokenizer = new XPathTokenizer( locationPath );
		if ( tokenizer.containsPredicates() )
		{
			return ofNullable( (T) new PredicatelessXPathConstraintViolation( locationPath ) );
		}
		else
		{
			return empty();
		}
	}
}
