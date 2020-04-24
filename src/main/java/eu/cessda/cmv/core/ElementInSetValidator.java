package eu.cessda.cmv.core;

import static java.util.Optional.empty;

import java.util.Optional;
import java.util.Set;

class ElementInSetValidator implements Validator.V10
{
	private String element;
	private Set<String> elementSet;

	ElementInSetValidator( String element, Set<String> elementSet )
	{
		this.element = element;
		this.elementSet = elementSet;
	}

	@Override
	public <T extends ConstraintViolation> Optional<T> validate()
	{
		if ( !elementSet.contains( element ) )
		{
			return Optional.of( (T) new ElementInSetConstraintViolation( element, elementSet ) );
		}
		else
		{
			return empty();
		}
	}
}
