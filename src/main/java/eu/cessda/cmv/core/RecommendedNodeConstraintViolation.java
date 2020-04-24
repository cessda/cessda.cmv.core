package eu.cessda.cmv.core;

class RecommendedNodeConstraintViolation extends NodeConstraintViolation
{
	RecommendedNodeConstraintViolation( String xPath )
	{
		super( xPath );
	}

	@Override
	public String getMessage()
	{
		return String.format( "'%s' is recommended", xPath );
	}
}
