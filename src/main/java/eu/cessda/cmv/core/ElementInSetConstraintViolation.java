package eu.cessda.cmv.core;

import java.util.Set;

class ElementInSetConstraintViolation implements ConstraintViolation.V10
{
	private String element;
	private Set<String> elementSet;

	ElementInSetConstraintViolation( String element, Set<String> elementSet )
	{
		this.element = element;
		this.elementSet = elementSet;
	}

	@Override
	public String getMessage()
	{
		return String.format( "'%s' is not element in '%s'", element, elementSet );
	}
}
