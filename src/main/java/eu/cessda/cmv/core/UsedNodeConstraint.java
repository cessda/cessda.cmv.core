package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.gesis.commons.xml.DomDocument;
import org.w3c.dom.Node;

abstract class UsedNodeConstraint implements Constraint.V10
{
	private DomDocument.V11 metadataDocument;
	private DomDocument.V11 profileDocument;
	private Class<? extends UsedNodeConstraintViolation> violationClass;

	public UsedNodeConstraint( DomDocument.V11 metadataDocument,
			DomDocument.V11 profileDocument,
			Class<? extends UsedNodeConstraintViolation> violationClass )
	{
		requireNonNull( metadataDocument );
		requireNonNull( profileDocument );
		requireNonNull( violationClass );

		this.metadataDocument = metadataDocument;
		this.profileDocument = profileDocument;
		this.violationClass = violationClass;
	}

	@SuppressWarnings( "unchecked" )
	public List<ConstraintViolation.V10> validate()
	{
		List<String> documentXPaths = metadataDocument.getNodeXPaths();
		boolean isRequired = violationClass == MandatoryNodeConstraintViolation.class;
		String xPath = String.format( "/DDIProfile/Used[@isRequired='%s']/@xpath", isRequired );
		return profileDocument.selectNodes( xPath ).stream()
				.map( Node::getTextContent )
				.filter( profileXPath -> !documentXPaths.contains( profileXPath ) )
				.map( profileXPath ->
				{
					try
					{
						return Optional.of( violationClass.getConstructor( String.class ).newInstance( profileXPath ) );
					}
					catch (Exception e)
					{
						return Optional.empty();
					}
				} )
				.filter( Optional::isPresent ).map( Optional::get )
				.map( ConstraintViolation.V10.class::cast )
				.collect( Collectors.toList() );
	}

}
