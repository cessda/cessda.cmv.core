package eu.cessda.cmv.core;

import static org.gesis.commons.resource.Resource.newResource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.gesis.commons.xml.DomDocument;
import org.gesis.commons.xml.XercesXalanDocument;

class DomMetadataDocumentValidator implements Constraint.V10
{
	private List<Constraint> constraints;

	public DomMetadataDocumentValidator( URI metadataDocumentUri, URI profileDocumentUri )
	{
		DomDocument.V11 metadataDocument = newDocument( metadataDocumentUri );
		DomDocument.V11 profileDocument = newDocument( profileDocumentUri );

		constraints = new ArrayList<>();
		constraints.add( new MandatoryNodeConstraint( metadataDocument, profileDocument ) );
		constraints.add( new RecommendedNodeConstraint( metadataDocument, profileDocument ) );
	}

	private DomDocument.V11 newDocument( URI uri )
	{
		return XercesXalanDocument.newBuilder()
				.ofInputStream( newResource( uri ).readInputStream() )
				.namespaceUnaware()
				.printPrettyWithIndentation( 2 )
				.build();
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
