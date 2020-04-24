package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

class CodeValueOfControlledVocabularyContraint implements Constraint.V20
{
	private String locationPath;

	public CodeValueOfControlledVocabularyContraint( String locationPath )
	{
		this.locationPath = locationPath;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document document )
	{
		requireNonNull( document );
		Document.V10 d = (Document.V10) document;

		List<Validator.V10> validators = new ArrayList<>();
		for ( Node node : d.getNodes( locationPath ) )
		{
			Validator.V10 validator = new CodeValueOfControlledVocabularyValidator( locationPath, node.getTextContent(),
					"uri" );
			validators.add( validator );
		}
		return (List<T>) validators;
	}
}
