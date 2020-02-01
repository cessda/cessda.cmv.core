package eu.cessda.cmv.core;

import org.gesis.commons.xml.DomDocument;

class RecommendedNodeConstraint extends UsedNodeConstraint
{
	public RecommendedNodeConstraint( DomDocument.V10 metadataDocument, DomDocument.V10 profileDocument )
	{
		super( metadataDocument, profileDocument, RecommendedNodeConstraintViolation.class );
	}
}
