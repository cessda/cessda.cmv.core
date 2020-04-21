package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.Factory.newDomDocument;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.gesis.commons.xml.DomDocument;
import org.gesis.commons.xml.ddi.DdiInputStream;

public class DomMetadataDocumentValidationGate implements Constraint.V10
{
	private List<Constraint> constraints;
	private DomDocument.V11 metadataDocument;
	private DomDocument.V11 profileDocument;

	public DomMetadataDocumentValidationGate(
			InputStream metadataDocumentInputStream,
			InputStream profileDocumentInputStream )
	{
		metadataDocument = newDomDocument( new DdiInputStream( metadataDocumentInputStream ) );
		profileDocument = newDomDocument( new DdiInputStream( profileDocumentInputStream ) );

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

	protected List<Constraint> getConstraints()
	{
		return constraints;
	}

	protected DomDocument.V11 getMetadataDocument()
	{
		return metadataDocument;
	}

	protected DomDocument.V11 getProfileDocument()
	{
		return profileDocument;
	}
}
