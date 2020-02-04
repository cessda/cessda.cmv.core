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
		DomDocument.V11 metadataDocument = newDocument( new File( "src/test/resources/ddi-v25/ukds-7481.xml" ) );
		List<String> doucmentXPaths = metadataDocument.getNodeXPaths();
		assertThat( doucmentXPaths, hasSize( 91 ) );

		DomDocument.V11 profileDocument = newDocument( new File( "src/test/resources/ddi-v25/cdc_profile.xml" ) );
		assertFalse( validateProfile( profileDocument ) );
	}

	@Test
	private void validateMetadataDocument()
	{
		Constraint.V10 metadataDocumentValidator = new DomMetadataDocumentValidator(
				new File( "src/test/resources/ddi-v25/ukds-7481.xml" ).toURI(),
				new File( "src/test/resources/ddi-v25/cdc_profile.xml" ).toURI() );
		List<ConstraintViolation.V10> constraintViolations = metadataDocumentValidator.validate();
		assertThat( constraintViolations, hasSize( 56 ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv instanceof MandatoryNodeConstraintViolation )
				.collect( Collectors.toList() ), hasSize( 31 ) );
		assertThat( constraintViolations.stream()
				.filter( cv -> cv instanceof RecommendedNodeConstraintViolation )
				.collect( Collectors.toList() ), hasSize( 25 ) );
		print( constraintViolations );
		assertFalse( constraintViolations.isEmpty() );
	}

	private void print( List<ConstraintViolation.V10> constraintViolations )
	{
		constraintViolations.stream()
				.map( ConstraintViolation.V10.class::cast )
				.map( ConstraintViolation.V10::getMessage )
				.forEach( System.out::println );
	}

	private boolean validateProfile( DomDocument.V11 profileDocument )
	{
		List<ConstraintViolation.V10> constraintViolations = new ArrayList<>();
		Constraint.V10 compilableXPathConstraint = new CompilableXPathConstraint( profileDocument );
		constraintViolations.addAll( compilableXPathConstraint.validate() );
		assertThat( constraintViolations, hasSize( 11 ) );
		return constraintViolations.isEmpty();
	}

	public static DomDocument.V11 newDocument( File file )
	{
		return newDocument( file.toURI() );
	}

	private static DomDocument.V11 newDocument( URI uri )
	{
		Resource resource = new TextResource( newResource( uri ) );
		return XercesXalanDocument.newBuilder()
				.ofContent( resource.toString() )
				.namespaceUnaware()
				.printPrettyWithIndentation( 2 )
				.build();
	}
}