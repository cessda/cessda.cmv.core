package eu.cessda.cmv.core;

import static java.util.function.Predicate.not;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.gesis.commons.xml.DomDocument;
import org.w3c.dom.Node;

class CodeValueOfControlledVocabularyContraint extends MandatoryNodeConstraint
{
	public CodeValueOfControlledVocabularyContraint( DomDocument.V11 metadataDocument, DomDocument.V11 profileDocument )
	{
		super( metadataDocument, profileDocument );
	}

	@Override
	@SuppressWarnings( { "unchecked", "java:S1075" } )
	public List<ConstraintViolation.V10> validate()
	{
		List<ConstraintViolation.V10> violations = super.validate();

		String constraintPath = "/DDIProfile/Used[contains(Instructions/Content, 'CodeValueOfControlledVocabulary')]";
		getProfileDocument().selectNodes( constraintPath ).forEach( usedNode ->
		{
			for ( String nodePath : getProfileDocument()
					.selectNodes( usedNode, "./@xpath" ).stream()
					.map( Node::getTextContent )
					.collect( Collectors.toSet() ) )
			{
				getMetadataDocument()
						.selectNodes( nodePath + "/@vocabURI" ).stream()
						.map( Node::getTextContent )
						.distinct()
						.forEach( cvUri ->
						{
							// cvUri not used yet for cv repo
							ControlledVocabularyRepository repository = new DdiAnalysisUnit10InMemoryControlledVocabularyRepository();
							Set<String> codeValuesInControlledVocabulary = repository.findCodeValues();
							Set<String> codeValuesInDocument = getMetadataDocument()
									.selectNodes( nodePath + "[@vocabURI='" + cvUri + "']" ).stream()
									.map( Node::getTextContent )
									.collect( Collectors.toSet() );
							codeValuesInDocument.stream()
									.filter( not( codeValuesInControlledVocabulary::contains ) )
									.forEach(
											codeValue -> violations.add( newViolation( nodePath, codeValue, cvUri ) ) );
						} );
			}
		} );
		return violations;
	}

	private ConstraintViolation.V10 newViolation( String nodePath, String codeValue, String cvUri )
	{
		return new CodeValueOfControlledVocabularyContraintViolation( nodePath, codeValue, cvUri );
	}
}
