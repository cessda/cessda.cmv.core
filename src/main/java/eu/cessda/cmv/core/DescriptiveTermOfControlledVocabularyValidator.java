package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;
import java.util.Set;

class DescriptiveTermOfControlledVocabularyValidator implements Validator.V10
{
	private DescriptiveTermNode node;

	DescriptiveTermOfControlledVocabularyValidator( DescriptiveTermNode node )
	{
		requireNonNull( node );
		this.node = node;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		if ( node.getControlledVocabularyRepository() == null )
		{
			String message = "Descriptive term '%s' in '%s' is not validateable because no controlled vocabulary is declared";
			message = String.format( message, node.getTextContent(), node.getLocationPath() );
			return of( new ConstraintViolation( message, node.getLocationInfo() ) );
		}
		else
		{
			Set<String> elementSet = node.getControlledVocabularyRepository().findDescriptiveTerms();
			if ( !elementSet.contains( node.getTextContent() ) )
			{
				String message = "Descriptive term '%s' in '%s' is not element of the controlled vocabulary in '%s'";
				message = String.format( message, node.getTextContent(), node.getLocationPath(),
						"TODO node.getControlledVocabularyRepositoryUri()" );
				return of( new ConstraintViolation( message, node.getLocationInfo() ) );
			}
			else
			{
				return empty();
			}
		}
	}
}
