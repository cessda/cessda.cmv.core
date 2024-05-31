package eu.cessda.cmv.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static javax.xml.XMLConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NamespaceContextImplTest
{
	@Test
	void shouldRetrieveNamespacesAndPrefixes()
	{
		HashMap<String, String> exampleMapping = new HashMap<>();
		exampleMapping.put( "", "default-namespace" ); // Test default mapping;
		exampleMapping.put( "ns1", "namespace-one" );

		// Create a new instance
		NamespaceContextImpl nsContext  = new NamespaceContextImpl(exampleMapping);

		// Assert mappings are as expected
		for ( Map.Entry<String, String> mapping : exampleMapping.entrySet() )
		{
			assertThat( nsContext.getNamespaceURI( mapping.getKey() ), equalTo( mapping.getValue() ) );
			assertThat( nsContext.getPrefix( mapping.getValue() ), equalTo( mapping.getKey() ) );
		}

		// Assert unmapped prefixes behave as expected
		assertThat( nsContext.getNamespaceURI( "unmapped-prefix" ), equalTo( NULL_NS_URI ) );
		assertThat( nsContext.getPrefix( "unmapped-namespace-uri" ), nullValue() );
	}

	@Test
	void shouldRetrieveDefaultMappings()
	{
		// Create a new instance
		NamespaceContextImpl nsContext  = new NamespaceContextImpl();

		// Clear any mappings
		nsContext.clear();

		// Assert default mappings
		{
			assertThat( nsContext.getNamespaceURI( XML_NS_PREFIX ), equalTo( XML_NS_URI ) );
			assertThat( nsContext.getPrefix( XML_NS_URI ), equalTo( XML_NS_PREFIX ) );
			Iterator<String> prefixes = nsContext.getPrefixes( XML_NS_URI );
			assertThat( prefixes.hasNext(), equalTo( true ) );
			assertThat( prefixes.next(), equalTo( XML_NS_PREFIX ) );
			assertThat( prefixes.hasNext(), equalTo( false ) );
			assertThrows( IllegalArgumentException.class, () -> nsContext.removeBinding( XML_NS_PREFIX ) );
		}

		{
			assertThat( nsContext.getNamespaceURI( XMLNS_ATTRIBUTE ), equalTo( XMLNS_ATTRIBUTE_NS_URI ) );
			assertThat( nsContext.getPrefix( XMLNS_ATTRIBUTE_NS_URI ), equalTo( XMLNS_ATTRIBUTE ) );
			Iterator<String> prefixes = nsContext.getPrefixes( XMLNS_ATTRIBUTE_NS_URI );
			assertThat( prefixes.hasNext(), equalTo( true ) );
			assertThat( prefixes.next(), equalTo( XMLNS_ATTRIBUTE ) );
			assertThat( prefixes.hasNext(), equalTo( false ) );
			assertThrows( IllegalArgumentException.class, () -> nsContext.removeBinding( XMLNS_ATTRIBUTE ) );
		}
	}

	@Test
	void shouldReplaceBindings()
	{
		String prefix = "prefix";
		String namespaceOne = "namespace-one";
		String namespaceTwo = "namespace-two";

		HashMap<String, String> exampleMapping = new HashMap<>();
		exampleMapping.put( prefix, namespaceOne );

		// Create a new instance
		NamespaceContextImpl nsContext  = new NamespaceContextImpl(exampleMapping);

		// Replace the binding
		assertThat( nsContext.bindNamespaceURI( prefix, namespaceTwo ), equalTo( namespaceOne ) );
		assertThat( nsContext.getNamespaceURI( prefix ), equalTo( namespaceTwo ) );

		// Remove the binding
		assertThat( nsContext.removeBinding( prefix ), equalTo( namespaceTwo ) );
		assertThat( nsContext.getNamespaceURI( prefix ), equalTo( NULL_NS_URI ) );
	}

	@ParameterizedTest
	@ValueSource(strings = { XML_NS_PREFIX, XMLNS_ATTRIBUTE })
	void shouldThrowWhenOverridingDefaultPrefixMappings( String prefix )
	{
		NamespaceContextImpl nsContext  = new NamespaceContextImpl();
		assertThrows( IllegalArgumentException.class, () -> nsContext.bindNamespaceURI( prefix, "override" ) );
	}

	@ParameterizedTest
	@ValueSource(strings = { XML_NS_URI, XMLNS_ATTRIBUTE_NS_URI })
	void shouldThrowWhenOverridingDefaultNamespaceMappings( String namespaceURI )
	{
		NamespaceContextImpl nsContext  = new NamespaceContextImpl();
		assertThrows( IllegalArgumentException.class, () -> nsContext.bindNamespaceURI( "prefix", namespaceURI ) );
	}
}
