package eu.cessda.cmv.core;

class RecommendedNodeConstraintViolation extends UsedNodeConstraintViolation
{
	public RecommendedNodeConstraintViolation( String xPath )
	{
		super( xPath );
	}

	@Override
	public String getMessage()
	{
		return String.format( "'%s' is recommended", xPath );
	}
}
