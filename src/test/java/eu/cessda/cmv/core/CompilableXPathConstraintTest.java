package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.Factory.newDomDocument;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

public class CompilableXPathConstraintTest
{
	@Test
	public void validate()
	{
		File file = new File( "src/test/resources/profiles/not-compilable-xpaths.xml" );
		CompilableXPathConstraint constraint = new CompilableXPathConstraint( newDomDocument( file ) );
		List<ConstraintViolation.V10> violations = constraint.validate();
		assertThat( violations, hasSize( 2 ) );
		assertThat( violations.get( 0 ).getMessage(), containsString( "A location step was expected" ) );
		assertThat( violations.get( 1 ).getMessage(), containsString( "Extra illegal tokens" ) );
	}
}
