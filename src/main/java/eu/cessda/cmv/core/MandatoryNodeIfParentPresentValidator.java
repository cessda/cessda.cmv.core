package eu.cessda.cmv.core;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.gesis.commons.xml.xpath.XPathTokenizer.PARENT;

import java.util.Optional;

import org.gesis.commons.xml.xpath.XPathTokenizer;

class MandatoryNodeIfParentPresentValidator implements Validator.V10
{
	private XPathTokenizer tokenizer;
	private Node parentNode;

	public MandatoryNodeIfParentPresentValidator( XPathTokenizer tokenizer, Node parentNode )
	{
		this.tokenizer = tokenizer;
		this.parentNode = parentNode;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		if ( parentNode.getChildCount( tokenizer.getLocationPathRelativeTo( PARENT ) ) == 0 )
		{
			return of( newConstraintViolation() );
		}
		else
		{
			return empty();
		}
	}

	protected ConstraintViolation newConstraintViolation()
	{
		String message = "'%s' is mandatory in %s";
		message = String.format( message,
				tokenizer.getLocationPathRelativeTo( PARENT ),
				tokenizer.getLocationPath( PARENT ) );
		return new ConstraintViolation( message, parentNode.getLocationInfo() );
	}
}
