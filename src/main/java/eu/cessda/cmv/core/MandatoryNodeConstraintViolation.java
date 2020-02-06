package eu.cessda.cmv.core;

class MandatoryNodeConstraintViolation extends UsedNodeConstraintViolation
{
	public MandatoryNodeConstraintViolation( String xPath )
	{
		super( xPath );
	}

	@Override
	public String getMessage()
	{
		return String.format( "'%s' is mandatory", xPath );
	}
}
