package eu.cessda.cmv.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.gesis.commons.xml.DomDocument;

class MetadataDocumentValidator implements Constraint.V10
{
	private List<Constraint> constraints;

	public MetadataDocumentValidator( DomDocument.V11 metadataDocument, DomDocument.V11 profileDocument )
	{
		constraints = new ArrayList<>();
		constraints.add( new MandatoryNodeConstraint( metadataDocument, profileDocument ) );
		constraints.add( new RecommendedNodeConstraint( metadataDocument, profileDocument ) );
	}

	@SuppressWarnings( "unchecked" )
	public List<ConstraintViolation.V10> validate()
	{
		return constraints.stream()
				.map( Constraint.V10.class::cast )
				.map( Constraint.V10::validate )
				.flatMap( List::stream )
				.map( ConstraintViolation.V10.class::cast )
				.collect( Collectors.toList() );
	}
}
