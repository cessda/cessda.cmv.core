package eu.cessda.cmv.core;

import static eu.cessda.cmv.core.Factory.newDocument;
import static org.gesis.commons.test.hamcrest.OptionalMatchers.isEmpty;
import static org.gesis.commons.test.hamcrest.OptionalMatchers.isPresent;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

public class CompilableXPathConstraintTest
{
	@Test
	public void test()
	{
		// given
		URL url = getClass().getResource( "/profiles/not-compilable-xpaths.xml" );

		// when
		Constraint.V20 constraint = new CompilableXPathConstraint();
		List<Validator.V10> validators = constraint.newValidators( newDocument( url ) );

		// then
		assertThat( validators, hasSize( 3 ) );
		Optional<ConstraintViolation.V10> result;
		result = validators.get( 0 ).validate();
		assertThat( result, isPresent() );
		assertThat( result.get().getMessage(), containsString( "A location step was expected" ) );
		result = validators.get( 1 ).validate();
		assertThat( result, isPresent() );
		assertThat( result.get().getMessage(), containsString( "Extra illegal tokens" ) );
		result = validators.get( 2 ).validate();
		assertThat( result, isEmpty() );
	}
}
