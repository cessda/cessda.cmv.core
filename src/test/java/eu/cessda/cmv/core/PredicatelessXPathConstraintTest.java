package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.Factory.newDomDocument;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

public class PredicatelessXPathConstraintTest
{
	@Test
	public void validate()
	{
		// https://bitbucket.org/cessda/cessda.cmv.core/issues/39

		File file = new File( "src/test/resources/profiles/xpaths-with-predicate.xml" );
		Constraint.V10 constraint = new PredicatelessXPathConstraint( newDomDocument( file ) );
		List<ConstraintViolation.V10> violations = constraint.validate();
		assertThat( violations, hasSize( 1 ) );
		assertThat( violations.get( 0 ).getMessage(), containsString( "contains a predicate" ) );
	}
}
