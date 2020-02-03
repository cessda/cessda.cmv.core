package eu.cessda.cmv.core;

import static org.gesis.commons.resource.Resource.newResource;
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

public class ValidationTest
{
	@Test
	public void inspectDocuments() throws IOException
	{
		DomDocument.V11 profileDocument = newDocument( new File( "src/test/resources/ddi-v25/cdc_profile.xml" ) );
		DomDocument.V11 metadataDocument = newDocument( new File( "src/test/resources/ddi-v25/ukds-7481.xml" ) );
		// TODO: DomDocument.V11::getNodeXPaths() also with attributes
		List<String> doucmentXPaths = metadataDocument.getNodeXPaths();
		assertThat( doucmentXPaths, hasSize( 91 ) );
		assertFalse( validateProfile( profileDocument ) );
		assertFalse( validateMetadata( metadataDocument, profileDocument ) );
	}

	private boolean validateMetadata( DomDocument.V11 metadataDocument, DomDocument.V11 profileDocument )
	{
		List<Constraint.V10> constraints = new ArrayList<>();
		constraints.add( new MandatoryNodeConstraint( metadataDocument, profileDocument ) );
		constraints.add( new RecommendedNodeConstraint( metadataDocument, profileDocument ) );
		List<ConstraintViolation> constraintViolations = constraints.stream()
				.map( constraint -> constraint.validate() )
				.flatMap( List::stream )
				.collect( Collectors.toList() );
		assertThat( constraintViolations, hasSize( 56 ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv instanceof MandatoryNodeConstraintViolation )
				.collect( Collectors.toList() ), hasSize( 31 ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv instanceof RecommendedNodeConstraintViolation )
				.collect( Collectors.toList() ), hasSize( 25 ) );
		constraintViolations.stream()
				.map( ConstraintViolation.V10.class::cast )
				.map( ConstraintViolation.V10::getMessage )
				.forEach( System.out::println );
		return constraintViolations.isEmpty();
	}

	private boolean validateProfile( DomDocument.V11 profileDocument )
	{
		List<ConstraintViolation.V10> constraintViolations = new ArrayList<>();
		Constraint.V10 compilableXPathConstraint = new CompilableXPathConstraint( profileDocument );
		constraintViolations.addAll( compilableXPathConstraint.validate() );
		assertThat( constraintViolations, hasSize( 11 ) );
		return constraintViolations.isEmpty();
	}

	private DomDocument.V11 newDocument( File file )
	{
		return newDocument( file.toURI() );
	}

	private DomDocument.V11 newDocument( URI uri )
	{
		Resource resource = new TextResource( newResource( uri ) );
		return XercesXalanDocument.newBuilder()
				.ofContent( resource.toString() )
				.namespaceUnaware()
				.printPrettyWithIndentation( 2 )
				.build();
	}
}