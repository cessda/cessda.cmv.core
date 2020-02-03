package eu.cessda.cmv.core;

import org.gesis.commons.xml.DomDocument;

class MandatoryNodeConstraint extends UsedNodeConstraint
{
	public MandatoryNodeConstraint( DomDocument.V11 metadataDocument, DomDocument.V11 profileDocument )
	{
		super( metadataDocument, profileDocument, MandatoryNodeConstraintViolation.class );
	}
}
