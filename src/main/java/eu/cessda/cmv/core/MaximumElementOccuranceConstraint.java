package eu.cessda.cmv.core;

import static java.util.Arrays.asList;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;

public class MaximumElementOccuranceConstraint extends NodeConstraint
{
	private long maxOccurs;

	public static final List<String> PROPERTIES = asList(
			MaximumElementOccuranceConstraint.class.getDeclaredConstructors() ).stream()
					.map( constructor -> asList( constructor.getParameters() ) )
					.flatMap( List::stream )
					.filter( Parameter::isNamePresent )
					.map( Parameter::getName )
					.collect( Collectors.toList() );

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
