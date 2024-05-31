/*-
 * #%L
 * cmv-core
 * %%
 * Copyright (C) 2020 - 2024 CESSDA ERIC
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package eu.cessda.cmv.core;

import eu.cessda.cmv.core.mediatype.profile.PrefixMap;

import javax.xml.namespace.NamespaceContext;
import java.util.*;

import static java.util.Objects.requireNonNull;
import static javax.xml.XMLConstants.*;

/**
 * Fully compliant implementation of {@link NamespaceContext}.
 */
public class NamespaceContextImpl implements NamespaceContext
{
	private static final String PREFIX_NULL = "prefix is null";
	private static final String NAMESPACE_URI_NULL = "namespaceURI is null";

	private final HashMap<String, String> prefixToNamespaceURI = new HashMap<>();
	private final HashMap<String, Set<String>> namespaceURIToPrefixes = new HashMap<>();

	/**
	 * Construct a new instance of NamespaceContext with no bindings.
	 */
	public NamespaceContextImpl()
	{
	}

	/**
	 * Construct a new instance of NamespaceContext with the bindings set using the given map.
	 *
	 * @param bindings the bindings to set, the prefix is the key, the namespace is the value.
	 */
	public NamespaceContextImpl( Map<String, String> bindings )
	{
		bindings.forEach( this::bindNamespaceURI );
	}

	/**
	 * Construct a new instance of NamespaceContext with the bindings set using the list of prefix maps.
	 *
	 * @param prefixMaps a list of prefix maps.
	 */
	NamespaceContextImpl( List<PrefixMap> prefixMaps )
	{
		for ( PrefixMap prefixMap : prefixMaps )
		{
			bindNamespaceURI( prefixMap.getPrefix(), prefixMap.getNamespace() );
		}
	}

	@Override
	public String getNamespaceURI( String prefix )
	{
		if (prefix == null)
		{
			throw new IllegalArgumentException( PREFIX_NULL );
		}

		switch ( prefix )
		{
			case DEFAULT_NS_PREFIX:
				return NULL_NS_URI;
			case XML_NS_PREFIX:
				return XML_NS_URI;
			case XMLNS_ATTRIBUTE:
				return XMLNS_ATTRIBUTE_NS_URI;
			default:
				return prefixToNamespaceURI.getOrDefault( prefix, NULL_NS_URI );
		}
	}

	@Override
	public String getPrefix( String namespaceURI )
	{
		if ( namespaceURI == null )
		{
			throw new IllegalArgumentException( NAMESPACE_URI_NULL );
		}

		switch ( namespaceURI )
		{
			case NULL_NS_URI:
				return DEFAULT_NS_PREFIX;
			case XML_NS_URI:
				return XML_NS_PREFIX;
			case XMLNS_ATTRIBUTE_NS_URI:
				return XMLNS_ATTRIBUTE;
			default:
				Set<String> prefixSet = namespaceURIToPrefixes.getOrDefault( namespaceURI, Collections.emptySet() );
				return prefixSet.stream().findAny().orElse( null );
		}
	}

	@Override
	public Iterator<String> getPrefixes( String namespaceURI )
	{
		if ( namespaceURI == null )
		{
			throw new IllegalArgumentException( NAMESPACE_URI_NULL );
		}

		Set<String> prefixSet;
		switch ( namespaceURI )
		{
			case XML_NS_URI:
				prefixSet = Collections.singleton( XML_NS_PREFIX );
				break;
			case XMLNS_ATTRIBUTE_NS_URI:
				prefixSet = Collections.singleton( XMLNS_ATTRIBUTE );
				break;
			default:
				prefixSet = namespaceURIToPrefixes.getOrDefault( namespaceURI, Collections.emptySet() );
				break;
		}

		return Collections.unmodifiableSet( prefixSet ).iterator();
	}

	/**
	 * Bind the prefix to the given namespace URI
	 *
	 * @param prefix the prefix
	 * @param namespaceURI the namespace to bind.
	 * @return the previously bound namespace, or {@code null} if there was no binding.
	 */
	public String bindNamespaceURI( String prefix, String namespaceURI )
	{
		requireNonNull( prefix, PREFIX_NULL );
		requireNonNull( namespaceURI, NAMESPACE_URI_NULL );

		// Fail mapping if the default prefixes are remapped
		if ( prefix.equals( DEFAULT_NS_PREFIX ) || prefix.equals( XML_NS_PREFIX ) || prefix.equals( XMLNS_ATTRIBUTE ) )
		{
			throw new IllegalArgumentException( "Prefix \"" + prefix + "\" cannot be rebound" );
		}

		// Fail mapping if the default namespaces are remapped
		if ( namespaceURI.equals( XML_NS_URI ) || namespaceURI.equals( XMLNS_ATTRIBUTE_NS_URI ) )
		{
			throw new IllegalArgumentException( "Namespace URI \"" + namespaceURI + "\" cannot be rebound" );
		}

		// Bind the prefix to the namespace URI
		String oldNamespaceURI = prefixToNamespaceURI.put( prefix, namespaceURI );
		if ( oldNamespaceURI != null )
		{
			// Remove the old prefix from the set of prefixes bound to the namespace URI
			namespaceURIToPrefixes.get( oldNamespaceURI ).remove( prefix );
		}

		// Add the prefix to the namespace URI's set of prefixes
		namespaceURIToPrefixes.computeIfAbsent( namespaceURI, k -> new HashSet<>() ).add( prefix );
		return oldNamespaceURI;
	}

	/**
	 * Remove the binding between a prefix and a namespace URI.
	 *
	 * @param prefix the prefix to unbind.
	 * @return the namespace URI that was bound, or {@code null} if there was no binding.
	 */
	public String removeBinding( String prefix )
	{
		Objects.requireNonNull( prefix, PREFIX_NULL );

		// Fail mapping if the default prefixes are remapped
		if ( prefix.equals( DEFAULT_NS_PREFIX ) || prefix.equals( XML_NS_PREFIX ) || prefix.equals( XMLNS_ATTRIBUTE ) )
		{
			throw new IllegalArgumentException( prefix + " cannot be unbound" );
		}

		// Remove the namespace URI from the prefix map
		String namespaceURI = prefixToNamespaceURI.remove( prefix );
		if ( namespaceURI != null )
		{
			// Remove the prefix from the set of prefixes bound to the namespace URI
			Set<String> prefixes = namespaceURIToPrefixes.get( namespaceURI );
			prefixes.remove( prefix );
			if ( prefixes.isEmpty() )
			{
				// No more prefixes are bound to the namespace URI, remove the set
				namespaceURIToPrefixes.remove( namespaceURI );
			}

			return namespaceURI;
		}
		else
		{
			return null;
		}
	}

	/**
	 * Return a map of prefix/namespace bindings.
	 */
	public Map<String, String> getAllBindings()
	{
		return Collections.unmodifiableMap( prefixToNamespaceURI );
	}

	/**
	 * Clear all bindings from the NamespaceContext.
	 */
	public void clear()
	{
		prefixToNamespaceURI.clear();
		namespaceURIToPrefixes.clear();
	}

	@Override
	public boolean equals( Object o )
	{
		if ( this == o ) return true;
		if ( !( o instanceof NamespaceContextImpl ) ) return false;
		NamespaceContextImpl that = (NamespaceContextImpl) o;
		return Objects.equals( prefixToNamespaceURI, that.prefixToNamespaceURI );
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( prefixToNamespaceURI );
	}
}
