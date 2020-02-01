package eu.cessda.cmv.core;

class CompilableXPathConstraintViolation implements ConstraintViolation.V10
{
	private String xPath;

	public CompilableXPathConstraintViolation( String xPath )
	{
		this.xPath = xPath;
	}

	@Override
	public String getMessage()
	{
		return String.format( "'%s' is not a compilable XPath", xPath );
	}
}
