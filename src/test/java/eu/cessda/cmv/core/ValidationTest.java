package eu.cessda.cmv.core;

import static java.lang.Boolean.FALSE;
import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;
import org.gesis.commons.xml.DomDocument;
import org.gesis.commons.xml.XercesXalanDocument;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

public class ValidationTest
{
	@Test
	public void inspectDocuments() throws IOException
	{
		DomDocument.V10 metadataDocument = newDocument( new File( "src/test/resources/ddi-v25/ukds-7481.xml" ) );
		List<String> doucmentXPaths = metadataDocument.getElementXPaths();
		assertThat( doucmentXPaths, hasSize( 73 ) );

		DomDocument.V10 profileDocument = newDocument( new File( "src/test/resources/ddi-v25/cdc_profile.xml" ) );
		assertThat( profileDocument.getElementXPaths(), hasSize( 17 ) );
		List<String> xPaths = profileDocument.selectNodes( "/DDIProfile/Used/@xpath" ).stream()
				.map( Node::getTextContent )
				.collect( Collectors.toList() );
		assertThat( xPaths, hasSize( 88 ) );

		assertFalse( validateProfile( profileDocument ) );
		assertFalse( validateMetadata( metadataDocument, profileDocument ) );

		// required XPaths
		List<String> requiredXPaths = profileDocument.selectNodes( "/DDIProfile/Used[@isRequired='true']/@xpath" )
				.stream()
				.map( Node::getTextContent )
				.collect( Collectors.toList() );
		assertThat( requiredXPaths, hasSize( 42 ) );
		xPaths = requiredXPaths.stream()
				.filter( requiredXPath -> !doucmentXPaths.contains( requiredXPath ) )
				.collect( Collectors.toList() );
		boolean isValid = xPaths.isEmpty();
		assertThat( isValid, equalTo( FALSE ) );

		// not required XPaths
		xPaths = profileDocument.selectNodes( "/DDIProfile/Used[@isRequired='false']/@xpath" ).stream()
				.map( Node::getTextContent )
				.collect( Collectors.toList() );
		assertThat( xPaths, hasSize( 88 - 42 ) );
	}

	private boolean validateMetadata( DomDocument.V10 metadataDocument, DomDocument.V10 profileDocument )
	{
		List<Constraint.V10> constraints = new ArrayList<>();
		constraints.add( new MandatoryNodeConstraint( metadataDocument, profileDocument ) );
		constraints.add( new RecommendedNodeConstraint( metadataDocument, profileDocument ) );
		List<ConstraintViolation> constraintViolations = constraints.stream()
				.map( constraint -> constraint.validate() )
				.flatMap( List::stream )
				.collect( Collectors.toList() );
		assertThat( constraintViolations, hasSize( 50 ) ); // ? 31 + 25 = 56
		constraintViolations.stream()
				.map( ConstraintViolation.V10.class::cast )
				.map( ConstraintViolation.V10::getMessage )
				.forEach( System.out::println );
		return constraintViolations.isEmpty();
	}

	private boolean validateProfile( DomDocument.V10 profileDocument )
	{
		List<ConstraintViolation.V10> constraintViolations = new ArrayList<>();
		Constraint.V10 compilableXPathConstraint = new CompilableXPathConstraint( profileDocument );
		constraintViolations.addAll( compilableXPathConstraint.validate() );
		assertThat( constraintViolations, hasSize( 11 ) );
		return constraintViolations.isEmpty();
	}

	private DomDocument.V10 newDocument( File file )
	{
		return newDocument( file.toURI() );
	}

	private DomDocument.V10 newDocument( URI uri )
	{
		Resource resource = new TextResource( newResource( uri ) );
		return XercesXalanDocument.newBuilder()
				.ofContent( resource.toString() )
				.namespaceUnaware()
				.printPrettyWithIndentation( 2 )
				.build();
	}
}