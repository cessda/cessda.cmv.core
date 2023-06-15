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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import static java.util.Objects.requireNonNull;
import static org.gesis.commons.resource.Resource.newResource;

public class CessdaMetadataValidatorFactory
{
	private static final Set<String> CONSTRAINTS = Collections.unmodifiableSet( new HashSet<>( Arrays.asList(
			"CompilableXPathConstraint",
			"ControlledVocabularyRepositoryConstraint",
			"FixedValueNodeConstraint",
			"MandatoryNodeConstraint",
			"MandatoryNodeIfParentPresentConstraint",
			"MaximumElementOccuranceConstraint",
			"NodeInProfileConstraint",
			"NotBlankNodeConstraint",
			"OptionalNodeConstraint",
			"PredicatelessXPathConstraint",
			"RecommendedNodeConstraint"
	) ) );

	private final XmlElementExtractor.V10 xmlElementExtractor;
	private final Tika documentMediaTypeDetector;

	public CessdaMetadataValidatorFactory()
	{
		xmlElementExtractor = new XercesXalanElementExtractor();
		documentMediaTypeDetector = new Tika( new CompositeDetector( new DdiDetector(), new OaipmhV20Detector() ) );
	}

	public DomDocument.V11 newDomDocument( File file ) throws IOException
	{
		try (FileInputStream inputStream = new FileInputStream(file) )
		{
			return newDomDocument(inputStream);
		}
	}

	public DomDocument.V11 newDomDocument( URI uri ) throws IOException
	{
		return newDomDocument( uri.toURL().openStream() );
	}

	public DomDocument.V11 newDomDocument( URL url ) throws IOException
	{
		return newDomDocument( url.openStream() );
	}

	public DomDocument.V11 newDomDocument( InputStream inputStream )
	{
		Objects.requireNonNull( inputStream );
		return XercesXalanDocument.newBuilder()
				.ofInputStream( inputStream )
				.namespaceAware()
				.printPrettyWithIndentation( 2 )
				.build();
	}

	public Document.V11 newDocument( File file )
	{
		return newDocument( newResource( file ).readInputStream() );
	}

	public Document.V11 newDocument( URI uri )
	{
		return newDocument( newResource( uri ).readInputStream() );
	}

	public Document.V11 newDocument( URL url )
	{
		return newDocument( newResource( url ).readInputStream() );
	}

	public Document.V11 newDocument( Resource resource )
	{
		return newDocument( requireNonNull( resource ).readInputStream() );
	}

	/**
	 * Returns a validation gate with the given name.
	 */
	public static ValidationGate.V10 getValidationGate( ValidationGateName name )
	{
		requireNonNull( name, "name must not be null" );
		return name.getValidationGate();
	}

	public Profile.V10 newProfile( Resource resource )
	{
		return new DomSemiStructuredDdiProfile( newDdiInputStream( resource ) );
	}

	public Profile.V10 newProfile( URI uri )
	{
		try
		{
			return newProfile( requireNonNull( uri ).toURL() );
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
		return newDdiInputStream( requireNonNull( resource ).readInputStream() );
	}

	public DdiInputStream newDdiInputStream( URL url )
	{
		return newDdiInputStream( newResource( url ).readInputStream() );
	}

	/**
	 * Returns a validation gate for the given constraints. The constraints are passed as the short name
	 * of the class (i.e. FixedValueNodeConstraint).
	 *
	 * @param constraints the constraints to be added
	 * @return the validation gate
	 * @throws InvalidGateException if any of the given constraints are invalid
	 */
	public static ValidationGate.V10 newValidationGate( Collection<String> constraints ) throws InvalidGateException
	{
		ArrayList<InvalidConstraintException> exceptions = new ArrayList<>();

		// Attempt to map the names of constraints to class objects
		List<Class<? extends Constraint.V20>> list = new ArrayList<>();
		for ( String c : constraints )
		{
			try
			{
				// Construct the fully qualified class name for the constraints
				Class<?> potentialConstraint = Class.forName( "eu.cessda.cmv.core." + c );
				list.add( potentialConstraint.asSubclass( Constraint.V20.class ) );
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

	@SuppressWarnings( "java:S1133" ) // false positive - XPath detected as a URI
	public Document.V11 newDocument( InputStream inputStream )
	{
		try ( InputStream bufferedInputStream = new BufferedInputStream( requireNonNull( inputStream ) ) )
		{
			String mediaType = documentMediaTypeDetector.detect( bufferedInputStream );
			if ( DdiDetector.MEDIATYPE.equals( mediaType ) )
			{
				return new DomCodebookDocument( bufferedInputStream );
			}
			else if ( OaipmhV20Detector.MEDIATYPE.equals( mediaType ) )
			{
				String xpath = "/OAI-PMH/GetRecord/record/metadata/*[1]";
				Optional<InputStream> extractedElementInputStream = xmlElementExtractor.extractAsInputStream( bufferedInputStream, xpath );
				if ( extractedElementInputStream.isPresent() )
				{
					return newDocument( extractedElementInputStream.get() );
				}
			}
			throw new NotDocumentException( String.format( "Detected media type: %s", mediaType ) );
		}
		catch ( XmlNotWellformedException e )
		{
			throw new NotDocumentException( String.format( "Not well-formed XML: %s", e.getMessage() ) );
		}
		catch ( IOException e )
		{
			throw new NotDocumentException( e );
		}
	}

	public DdiInputStream newDdiInputStream( InputStream inputStream )
	{
		try
		{
			return new DdiInputStream( inputStream );
		}
		catch ( IOException e )
		{
			throw new IllegalArgumentException( e );
		}
	}

	/**
	 * Returns a validation gate with the given name.
	 */
	ValidationGate.V10 newValidationGate( ValidationGateName name )
	{
		return getValidationGate( name );
	}

	public ValidationService.V10 newValidationService()
	{
		return new ValidationServiceV0( this );
	}
}
