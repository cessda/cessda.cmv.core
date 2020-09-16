package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

class FixedValueNodeValidator implements Validator.V10
{
	private Node node;
	private String fixedValue;

	public FixedValueNodeValidator( Node node, String fixedValue )
	{
		this.node = requireNonNull( node );
		this.fixedValue = requireNonNull( fixedValue );
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		if ( fixedValue.equals( node.getTextContent().trim() ) )
		{
			return empty();
		}
		else
		{
			return of( newConstraintViolation() );
		}
	}

	protected ConstraintViolation newConstraintViolation()
	{
		String message = "'%s' is not equal to fixed value '%s' in '%s'";
		message = String.format( message, node.getTextContent(), fixedValue, node.getLocationPath() );
		return new ConstraintViolation( message, node.getLocationInfo() );
	}
}
