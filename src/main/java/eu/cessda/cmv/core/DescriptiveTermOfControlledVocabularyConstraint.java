package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;

class DescriptiveTermOfControlledVocabularyConstraint extends NodeConstraint
{
	public DescriptiveTermOfControlledVocabularyConstraint( String locationPath )
	{
		super( locationPath );
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document document )
	{
		requireNonNull( document );
		return (List<T>) ((Document.V10) document).getNodes( getLocationPath() ).stream()
				.map( DescriptiveTermNode.class::cast )
				.map( DescriptiveTermOfControlledVocabularyValidator::new )
				.collect( Collectors.toList() );
	}
}