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

import org.apache.tika.Tika;
import org.apache.tika.detect.CompositeDetector;
import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.io.DdiDetector;
import org.gesis.commons.resource.io.DdiInputStream;
import org.gesis.commons.resource.io.OaipmhV20Detector;
import org.gesis.commons.xml.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import static java.util.Objects.requireNonNull;

public class CessdaMetadataValidatorFactory
{
	private static final Set<String> CONSTRAINTS = Collections.unmodifiableSet( new HashSet<>( Arrays.asList(
		"CompilableXPathConstraint",
		"ControlledVocabularyRepositoryConstraint",
		"FixedValueNodeConstraint",
		"MandatoryNodeConstraint",
		"MandatoryNodeIfParentPresentConstraint",
		"MaximumElementOccurrenceConstraint",
		"NodeInProfileConstraint",
		"NotBlankNodeConstraint",
		"OptionalNodeConstraint",
		"PredicatelessXPathConstraint",
		"RecommendedNodeConstraint"
	) ) );

	private final XmlElementExtractor.V10 xmlElementExtractor = new XercesXalanElementExtractor();
	private final Tika documentMediaTypeDetector = new Tika( new CompositeDetector( new DdiDetector(), new OaipmhV20Detector() ) );

	public DomDocument.V11 newDomDocument( File file ) throws IOException
	{
		try ( FileInputStream inputStream = new FileInputStream( file ) )
		{
			return newDomDocument( inputStream );
		}
	}

	public DomDocument.V11 newDomDocument( URI uri ) throws IOException
	{
		return newDomDocument( uri.toURL() );
	}

	public DomDocument.V11 newDomDocument( URL url ) throws IOException
	{
		try ( InputStream inputStream = url.openStream() )
		{
			return newDomDocument( inputStream );
		}
	}

	public DomDocument.V11 newDomDocument( InputStream inputStream )
	{
		Objects.requireNonNull( inputStream, "inputStream is not null" );
		return XercesXalanDocument.newBuilder()
			.ofInputStream( inputStream )
			.namespaceAware()
			.printPrettyWithIndentation( 2 )
			.build();
	}

	/**
	 * Parse the XML at the given file into a {@link Document}. The XML document must be either a DDI document
	 * or an OAI-PMH document with a valid DDI document in the {@code <metadata>} element.
	 *
	 * @param file the file where the XML to be parsed is located.
	 * @throws NotDocumentException if the XML is not well-formed, or is not detected as a DDI document.
	 * @throws IOException          if an IO error occurs.
	 */
	public Document newDocument( File file ) throws IOException, NotDocumentException
	{
		Objects.requireNonNull( file, "file is not null" );
		try ( FileInputStream inputStream = new FileInputStream( file ) )
		{
			return newDocument( inputStream );
		}
	}

	/**
	 * Parse the XML located at the given URI into a {@link Document}. The XML document must be either a DDI document
	 * or an OAI-PMH document with a valid DDI document in the {@code <metadata>} element.
	 *
	 * @param uri the URI where the XML to be parsed is located.
	 * @throws NotDocumentException  if the XML is not well-formed, or is not detected as a DDI document.
	 * @throws MalformedURLException if the given URI cannot be constructed to a URL
	 * @throws IOException           if an IO error occurs.
	 * @implNote this calls {@link #newDocument(URL)} using {@link URI#toURL()} to transform the URI to a URL.
	 */
	public Document newDocument( URI uri ) throws IOException, NotDocumentException
	{
		Objects.requireNonNull( uri, "uri is not null" );
		return newDocument( uri.toURL() );
	}

	/**
	 * Parse the XML located at the given URL into a {@link Document}. The XML document must be either a DDI document
	 * or an OAI-PMH document with a valid DDI document in the {@code <metadata>} element.
	 *
	 * @param url the URL where the XML to be parsed is located.
	 * @throws NotDocumentException if the XML is not well-formed, or is not detected as a DDI document.
	 * @throws IOException          if an IO error occurs.
	 */
	public Document newDocument( URL url ) throws IOException, NotDocumentException
	{
		Objects.requireNonNull( url, "url is not null" );
		try ( InputStream inputStream = url.openStream() )
		{
			return newDocument( inputStream );
		}
	}

	/**
	 * Parse the given resource into a {@link Document}. The XML document must be either a DDI document or an
	 * OAI-PMH document with a valid DDI document in the {@code <metadata>} element.
	 *
	 * @param resource the resource containing the XML to be parsed.
	 * @throws NotDocumentException if the XML is not well-formed, or is not detected as a DDI document.
	 * @throws IOException          if an IO error occurs.
	 */
	public Document newDocument( Resource resource ) throws IOException, NotDocumentException
	{
		Objects.requireNonNull( resource, "resource is not null" );
		try ( InputStream inputStream = resource.readInputStream() )
		{
			return newDocument( inputStream );
		}
	}

	/**
	 * Returns a validation gate with the given name.
	 */
	public static ValidationGate getValidationGate( ValidationGateName name )
	{
		requireNonNull( name, "name must not be null" );
		return name.getValidationGate();
	}

	public Profile newProfile( Resource resource ) throws IOException
	{
		Objects.requireNonNull( resource, "resource must not be null" );
		try ( DdiInputStream ddiInputStream = new DdiInputStream( resource ) )
		{
			return new DomSemiStructuredDdiProfile( ddiInputStream );
		}
	}

	public Profile newProfile( URI uri ) throws IOException
	{
		Objects.requireNonNull( uri, "uri must not be null" );
		return newProfile( uri.toURL() );
	}

	public Profile newProfile( URL url ) throws IOException
	{
		Objects.requireNonNull( url, "url must not be null" );
		InputStream urlInputStream = url.openStream();
		try ( DdiInputStream ddiInputStream = new DdiInputStream( urlInputStream ) )
		{
			return new DomSemiStructuredDdiProfile( ddiInputStream );
		}
	}

	public Profile newProfile( File file ) throws IOException
	{
		FileInputStream fileInputStream = new FileInputStream( file );
		try ( DdiInputStream ddiInputStream = new DdiInputStream( fileInputStream ) )
		{
			return new DomSemiStructuredDdiProfile( ddiInputStream );
		}
	}

	/**
	 * Returns a validation gate for the given constraints. The constraints are passed as the short name
	 * of the class (i.e. FixedValueNodeConstraint).
	 *
	 * @param constraints the constraints to be added
	 * @return the validation gate
	 * @throws InvalidGateException if any of the given constraints are invalid
	 */
	public static ValidationGate newValidationGate( Collection<String> constraints ) throws InvalidGateException
	{
		ArrayList<InvalidConstraintException> exceptions = new ArrayList<>();

		// Attempt to map the names of constraints to class objects
		List<Class<? extends Constraint>> list = new ArrayList<>();
		for ( String c : constraints )
		{
			try
			{
				// Construct the fully qualified class name for the constraints
				Class<?> potentialConstraint = Class.forName( "eu.cessda.cmv.core." + c );
				list.add( potentialConstraint.asSubclass( Constraint.class ) );
			}
			catch ( ClassNotFoundException | ClassCastException e )
			{
				exceptions.add( new InvalidConstraintException( c, e ) );
			}
		}


		if ( exceptions.isEmpty() )
		{
			// All constraints successfully added
			return new AbstractValidationGate( list )
			{
			};
		}
		else
		{
			throw new InvalidGateException( exceptions );
		}
	}

	/**
	 * Return a set of all known constraints.
	 * <p>
	 * This should be used with {@link CessdaMetadataValidatorFactory#newValidationGate(Collection)}
	 * to ensure that a validation gate is not constructed with invalid constraints.
	 */
	public static Set<String> getConstraints()
	{
		return CONSTRAINTS;
	}

	/**
	 * Parse the given XML into a {@link Document}. The XML document must be either a DDI document or an
	 * OAI-PMH document with a valid DDI document in the {@code <metadata>} element.
	 *
	 * @param inputStream the input stream containing the XML to be parsed.
	 * @throws NotDocumentException if the XML is not well-formed, or is not detected as a DDI document.
	 * @throws IOException          if an IO error occurs.
	 */
	public Document newDocument( InputStream inputStream ) throws IOException, NotDocumentException
	{
		Objects.requireNonNull( inputStream, "inputStream is null" );

		// Encapsulate the input stream into a BufferedInputStream if mark() is not supported
		if ( !inputStream.markSupported() )
		{
			inputStream = new BufferedInputStream( inputStream );
		}

		try
		{
			String mediaType = documentMediaTypeDetector.detect( inputStream );
			if ( DdiDetector.MEDIATYPE.equals( mediaType ) )
			{
				// Parse as DDI
				return new DomCodebookDocument( inputStream );
			}
			else if ( OaipmhV20Detector.MEDIATYPE.equals( mediaType ) )
			{
				// Try to extract <metadata> and recurse
				Optional<InputStream> extractedElementInputStream = xmlElementExtractor.extractAsInputStream( inputStream, "/OAI-PMH/GetRecord/record/metadata/*[1]" );
				if ( extractedElementInputStream.isPresent() )
				{
					return newDocument( extractedElementInputStream.get() );
				}
			}
			throw new NotDocumentException( String.format( "Detected media type: %s", mediaType ) );
		}
		catch ( XmlNotWellformedException e )
		{
			throw new NotDocumentException( e );
		}
	}

	/**
	 * Returns a validation gate with the given name.
	 */
	ValidationGate newValidationGate( ValidationGateName name )
	{
		return getValidationGate( name );
	}

	public ValidationService newValidationService()
	{
		return new ValidationServiceImpl( this );
	}
}
