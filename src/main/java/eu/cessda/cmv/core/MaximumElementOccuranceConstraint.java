package eu.cessda.cmv.core;

import static java.util.Arrays.asList;

import java.util.List;

class MaximumElementOccuranceConstraint extends NodeConstraint
{
	private long maxOccurs;

	public MaximumElementOccuranceConstraint( String locationPath, long maxOccurs )
	{
		super( locationPath );
		this.maxOccurs = maxOccurs;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document document )
	{
		long actualCount = ((Document.V10) document).getNodes( getLocationPath() ).size();
		return asList( (T) new MaximumElementOccuranceValidator( getLocationPath(), actualCount, maxOccurs ) );
	}
}
