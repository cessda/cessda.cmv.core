/*-
 * #%L
 * cmv-core
 * %%
 * Copyright (C) 2020 - 2021 CESSDA ERIC
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

import static eu.cessda.cmv.core.ValidationGateName.BASIC;
import static eu.cessda.cmv.core.ValidationGateName.BASICPLUS;
import static eu.cessda.cmv.core.ValidationGateName.EXTENDED;
import static eu.cessda.cmv.core.ValidationGateName.STANDARD;
import static eu.cessda.cmv.core.ValidationGateName.STRICT;
import static java.util.Objects.requireNonNull;
import static org.gesis.commons.resource.Resource.newResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.io.DdiInputStream;
import org.gesis.commons.xml.DomDocument;
import org.gesis.commons.xml.XercesXalanDocument;

public class CessdaMetadataValidatorFactory
{
	public DomDocument.V11 newDomDocument( File file )
	{
		requireNonNull( file );
		return newDomDocument( file.toURI() );
	}

	public DomDocument.V11 newDomDocument( URI uri )
	{
		requireNonNull( uri );
		Resource resource = newResource( uri );
		return newDomDocument( resource.readInputStream() );
	}

	public DomDocument.V11 newDomDocument( InputStream inputStream )
	{
		requireNonNull( inputStream );
		return XercesXalanDocument.newBuilder()
				.ofInputStream( inputStream )
				.namespaceAware()
				.printPrettyWithIndentation( 2 )
				.build();
	}

	public Document.V11 newDocument( File file )
	{
		try
		{
			requireNonNull( file );
			return newDocument( file.toURI().toURL() );
		}
		catch (MalformedURLException e)
		{
			throw new IllegalArgumentException( e );
		}
	}

	public Document.V11 newDocument( Resource resource )
	{
		return new DomCodebookDocument( newDdiInputStream( resource ) );
	}

	public Document.V11 newDocument( URI uri )
	{
		requireNonNull( uri );
		try
		{
			return newDocument( uri.toURL() );
		}
		catch (MalformedURLException e)
		{
			throw new IllegalArgumentException( e );
		}
	}

	public Document.V11 newDocument( URL url )
	{
		return new DomCodebookDocument( newDdiInputStream( url ) );
	}

	public Profile.V10 newProfile( Resource resource )
	{
		return new DomSemiStructuredDdiProfile( newDdiInputStream( resource ) );
	}

	public Profile.V10 newProfile( URI uri )
	{
		requireNonNull( uri );
		try
		{
			return newProfile( uri.toURL() );
		}
		catch (MalformedURLException e)
		{
			throw new IllegalArgumentException( e );
		}
	}

	public Profile.V10 newProfile( URL url )
	{
		return new DomSemiStructuredDdiProfile( newDdiInputStream( url ) );
	}

	public Profile.V10 newProfile( File file )
	{
		try
		{
			return newProfile( file.toURI().toURL() );
		}
		catch (MalformedURLException e)
		{
			throw new IllegalArgumentException( e );
		}
	}

	public DdiInputStream newDdiInputStream( Resource resource )
	{
		requireNonNull( resource );
		return newDdiInputStream( resource.readInputStream() );
	}

	public DdiInputStream newDdiInputStream( URL url )
	{
		requireNonNull( url );
		return newDdiInputStream( (Resource) newResource( url ) );
	}

	public DdiInputStream newDdiInputStream( InputStream inputStream )
	{
		try
		{
			return new DdiInputStream( inputStream );
		}
		catch (IOException e)
		{
			throw new IllegalArgumentException( e );
		}
	}

	@SuppressWarnings( "deprecation" )
	public ValidationGate.V10 newValidationGate( ValidationGateName name )
	{
		requireNonNull( name );
		if ( name.equals( BASIC ) )
		{
			return new BasicValidationGate();
		}
		else if ( name.equals( BASICPLUS ) )
		{
			return new BasicPlusValidationGate();
		}
		else if ( name.equals( STANDARD ) )
		{
			return new StandardValidationGate();
		}
		else if ( name.equals( EXTENDED ) )
		{
			return new ExtendedValidationGate();
		}
		else if ( name.equals( STRICT ) )
		{
			return new StrictValidationGate();
		}
		else
		{
			throw new IllegalArgumentException( name + " not supported" );
		}
	}

	public ValidationService.V10 newValidationService()
	{
		return new ValidationServiceV0( this );
	}
}
