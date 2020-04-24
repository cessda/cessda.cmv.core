package eu.cessda.cmv.core;

abstract class NodeConstraintViolation implements ConstraintViolation.V10
{
	protected String xPath;

	NodeConstraintViolation( String xPath )
	{
		this.xPath = xPath;
	}

	@Override
	public String getMessage()
	{
		return String.format( "'%s' is not used node", xPath );
	}
}
