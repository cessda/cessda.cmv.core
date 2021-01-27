package eu.cessda.cmv.core;

import java.util.ArrayList;
import java.util.List;

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepositoryProxy;

class ControlledVocabularyRepositoryConstraint implements Constraint.V20
{
	private String locationPath;
	private String type;
	private String uri;

	public ControlledVocabularyRepositoryConstraint(
			String locationPath,
			String repositoryType,
			String repositoryUri )
	{
		this.locationPath = locationPath;
		this.type = repositoryType;
		this.uri = repositoryUri;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document document )
	{
		Document.V11 d = (Document.V11) document;
		ControlledVocabularyRepositoryProxy proxy = new ControlledVocabularyRepositoryProxy( type, uri, true );
		d.register( uri, proxy );
		long count = d.getNodes( locationPath ).stream()
				.filter( node -> node.getTextContent().contentEquals( uri ) )
				.count();
		List<Validator> validators = new ArrayList<>();
		if ( count > 0 )
		{
			validators.add( new ControlledVocabularyRepositoryValidator( proxy ) );
		}
		return (List<T>) validators;
	}
}
