package eu.cessda.cmv.core;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import java.util.Optional;

class NotBlankNodeValidator implements Validator.V10
{
	private Node node;

	public NotBlankNodeValidator( Node node )
	{
		this.node = node;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		boolean isBlank = node.getTextContent().trim().length() == 0;
		if ( isBlank )
		{
			return ofNullable( newConstraintViolation() );
		}
		else
		{
			return empty();
		}
	}

	protected ConstraintViolation newConstraintViolation()
	{
		String message = "'%s' is blank";
		message = String.format( message, node.getLocationPath() );
		return new ConstraintViolation( message, node.getLocationInfo() );
	}
}
