package eu.cessda.cmv.core;

import org.gesis.commons.xml.DomDocument;

@Deprecated
class MandatoryNodeConstraintV1 extends UsedNodeConstraint
{
	public MandatoryNodeConstraintV1( DomDocument.V11 metadataDocument, DomDocument.V11 profileDocument )
	{
		super( metadataDocument, profileDocument, MandatoryNodeConstraintViolation.class );
	}
}
