package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.Factory.newDomDocument;
import static org.gesis.commons.resource.Resource.newResource;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;

public class PredicatelessXPathConstraintTest
{
	@Test
	public void validate()
	{
		// https://bitbucket.org/cessda/cessda.cmv.core/issues/39
		URL file = getClass().getResource( "/profiles/xpaths-with-predicate.xml" );
		Constraint.V10 constraint = new PredicatelessXPathConstraint( newDomDocument( newResource( file )
				.readInputStream() ) );
		List<ConstraintViolation.V10> violations = constraint.validate();
		assertThat( violations, hasSize( 1 ) );
		assertThat( violations.get( 0 ).getMessage(), containsString( "contains a predicate" ) );
	}
}
