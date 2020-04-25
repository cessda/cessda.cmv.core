package eu.cessda.cmv.core;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;
import java.util.Set;

class ElementInSetValidator implements Validator.V10
{
	private Node node;
	private Set<String> elementSet;

	ElementInSetValidator( Node node, Set<String> elementSet )
	{
		this.node = node;
		this.elementSet = elementSet;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		if ( !elementSet.contains( node.getTextContent() ) )
		{
			return of( newConstraintViolation() );
		}
		else
		{
			return empty();
		}
	}

	private ConstraintViolation newConstraintViolation()
	{
		String message = "'%s' is not element in '%s'";
		message = String.format( message, node.getTextContent(), elementSet );
		return new ConstraintViolation( message, node.getLocationInfo() );
	}
}
