package eu.cessda.cmv.core;

import static org.gesis.commons.test.hamcrest.OptionalMatchers.isEmpty;
import static org.gesis.commons.test.hamcrest.OptionalMatchers.isPresent;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class ElementInSetValidatorTest
{
	@Test
	public void validate()
	{
		Set<String> elementSet = new HashSet<>( Arrays.asList( "a", "B", "c", "C" ) );
		assertThat( elementSet, hasSize( 4 ) );
		assertThat( new ElementInSetValidator( "a", elementSet ).validate(), isEmpty() );
		assertThat( new ElementInSetValidator( "A", elementSet ).validate(), isPresent() );
		assertThat( new ElementInSetValidator( "B", elementSet ).validate(), isEmpty() );
		assertThat( new ElementInSetValidator( "b", elementSet ).validate(), isPresent() );
		assertThat( new ElementInSetValidator( "c", elementSet ).validate(), isEmpty() );
		assertThat( new ElementInSetValidator( "C", elementSet ).validate(), isEmpty() );
	}
}
