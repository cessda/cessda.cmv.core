package eu.cessda.cmv.core;

import org.gesis.commons.xml.DomDocument;

class RecommendedNodeConstraint extends UsedNodeConstraint
{
	public RecommendedNodeConstraint( DomDocument.V11 metadataDocument, DomDocument.V11 profileDocument )
	{
		super( metadataDocument, profileDocument, RecommendedNodeConstraintViolation.class );
	}
}
