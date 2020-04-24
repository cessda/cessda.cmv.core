package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.Factory.newDocument;
import static org.gesis.commons.test.hamcrest.OptionalMatchers.isEmpty;
import static org.gesis.commons.test.hamcrest.OptionalMatchers.isPresent;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;

public class PredicatelessXPathConstraintTest
{
	@Test
	public void test()
	{
		URL url = getClass().getResource( "/profiles/xpaths-with-predicate.xml" );

		Constraint.V20 constraint = new PredicatelessXPathConstraint();
		List<Validator.V10> validators = constraint.newValidators( newDocument( url ) );
		assertThat( validators, hasSize( 2 ) );
		Validator.V10 validator = validators.get( 0 );
		assertThat( validator.validate(), isEmpty() );
		validator = validators.get( 1 );
		assertThat( validator.validate(), isPresent() );
		assertThat( validator.validate()
				.map( ConstraintViolation.V10.class::cast )
				.map( ConstraintViolation.V10::getMessage )
				.get(), containsString( "contains a predicate" ) );
	}
}
