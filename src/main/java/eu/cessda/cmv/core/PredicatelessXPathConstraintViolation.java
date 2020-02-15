package eu.cessda.cmv.core;

class PredicatelessXPathConstraintViolation implements ConstraintViolation.V10
{
	private String xPath;

	public PredicatelessXPathConstraintViolation( String xPath )
	{
		this.xPath = xPath;
	}

	@Override
	public String getMessage()
	{
		return String.format( "'%s' contains a predicate", xPath );
	}
}
