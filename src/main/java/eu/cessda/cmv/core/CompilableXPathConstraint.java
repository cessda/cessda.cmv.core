package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;

import org.gesis.commons.xml.DomDocument;
import org.w3c.dom.Node;

class CompilableXPathConstraint implements Constraint.V10
{
	private CompilableXPathValidator compilableXPathValidator;
	private DomDocument.V10 profileDocument;

	public CompilableXPathConstraint( DomDocument.V10 profileDocument )
	{
		requireNonNull( profileDocument );
		this.profileDocument = profileDocument;
		compilableXPathValidator = new CompilableXPathValidator();
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public List<ConstraintViolation.V10> validate()
	{
		return profileDocument.selectNodes( "/DDIProfile/Used/@xpath" ).stream()
				.map( Node::getTextContent )
				.filter( xpath -> !compilableXPathValidator.isValid( xpath ) )
				.map( CompilableXPathConstraintViolation::new )
				.map( ConstraintViolation.V10.class::cast )
				.collect( Collectors.toList() );
	}
}
