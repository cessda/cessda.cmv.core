package eu.cessda.cmv.core;

class CompilableXPathConstraintViolation implements ConstraintViolation.V10
{
	private String reason;
	private String xPath;

	public CompilableXPathConstraintViolation( String xPath, String reason )
	{
		this.reason = reason;
		this.xPath = xPath;
	}

	@Override
	public String getMessage()
	{
		return String.format( "'%s' is not a compilable XPath: %s", xPath, reason );
	}
}
