package eu.cessda.cmv.core;

import java.util.ArrayList;
import java.util.List;

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
		// TODO Implement DescriptiveTermOfControlledVocabularyValidator #27
		List<Validator> validators = new ArrayList<>();
		return (List<T>) validators;
	}
}