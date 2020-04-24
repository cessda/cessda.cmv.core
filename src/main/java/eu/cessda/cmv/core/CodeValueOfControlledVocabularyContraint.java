package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;

class CodeValueOfControlledVocabularyContraint implements Constraint.V20
{
	private String locationPath;

	CodeValueOfControlledVocabularyContraint( String locationPath )
	{
		this.locationPath = locationPath;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document document )
	{
		requireNonNull( document );
		return (List<T>) ((Document.V10) document).getNodes( locationPath ).stream()
				.map( CodeValueNode.class::cast )
				.map( node -> new CodeValueOfControlledVocabularyValidator( node,
						new DdiAnalysisUnit10InMemoryControlledVocabularyRepository() ) )
				.collect( Collectors.toList() );
	}
}
