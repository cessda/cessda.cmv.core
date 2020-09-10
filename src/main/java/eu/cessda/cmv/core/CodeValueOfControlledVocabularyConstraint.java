package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;

class CodeValueOfControlledVocabularyConstraint implements Constraint.V20
{
	private String locationPath;

	public CodeValueOfControlledVocabularyConstraint( String locationPath )
	{
		requireNonNull( locationPath );
		this.locationPath = locationPath;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document document )
	{
		requireNonNull( document );
		return (List<T>) ((Document.V10) document).getNodes( locationPath ).stream()
				.map( ControlledVocabularyNode.class::cast )
				.map( CodeValueOfControlledVocabularyValidator::new )
				.collect( Collectors.toList() );
	}
}
