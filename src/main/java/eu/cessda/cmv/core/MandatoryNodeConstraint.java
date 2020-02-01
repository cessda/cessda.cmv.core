package eu.cessda.cmv.core;

import org.gesis.commons.xml.DomDocument;

class MandatoryNodeConstraint extends UsedNodeConstraint
{
	public MandatoryNodeConstraint( DomDocument.V10 metadataDocument, DomDocument.V10 profileDocument )
	{
		super( metadataDocument, profileDocument, MandatoryNodeConstraintViolation.class );
	}
}
