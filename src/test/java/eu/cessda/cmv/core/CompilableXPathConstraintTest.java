package eu.cessda.cmv.core;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static eu.cessda.cmv.core.Factory.newDomDocument;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class CompilableXPathConstraintTest
{
	@Test
	public void validate() throws URISyntaxException
	{
		URL file = getClass().getResource( "/profiles/not-compilable-xpaths.xml" );
		CompilableXPathConstraint constraint = new CompilableXPathConstraint( newDomDocument( file.toURI() ) );
		List<ConstraintViolation.V10> violations = constraint.validate();
		assertThat( violations, hasSize( 2 ) );
		assertThat( violations.get( 0 ).getMessage(), containsString( "A location step was expected" ) );
		assertThat( violations.get( 1 ).getMessage(), containsString( "Extra illegal tokens" ) );
	}
}
