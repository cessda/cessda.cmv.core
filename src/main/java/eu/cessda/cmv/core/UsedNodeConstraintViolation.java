package eu.cessda.cmv.core;

abstract class UsedNodeConstraintViolation implements ConstraintViolation.V10
{
	protected String xPath;

	public UsedNodeConstraintViolation( String xPath )
	{
		this.xPath = xPath;
	}

	@Override
	public String getMessage()
	{
		return String.format( "'%s' is not used node", xPath );
	}
}
