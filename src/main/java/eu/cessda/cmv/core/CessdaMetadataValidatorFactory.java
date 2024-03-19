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

import eu.cessda.cmv.core.controlledvocabulary.ControlledVocabularyRepository;
import org.gesis.commons.xml.XMLDocument;
import org.gesis.commons.xml.XmlNotWellformedException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
	private static final String OAI_PMH_XML_NAMESPACE = "http://www.openarchives.org/OAI/2.0/";

    private final ConcurrentHashMap<URI, ControlledVocabularyRepository> cvrMap = new ConcurrentHashMap<>();

	/**
	 * Parse the XML at the given file into a {@link Document}. The XML document must be either a DDI document
	 * or an OAI-PMH document with a valid DDI document in the {@code <metadata>} element.
	 *
	 * @param file the file where the XML to be parsed is located.
	 * @throws NotDocumentException if the XML is not well-formed.
	 * @throws IOException          if an IO error occurs.
	 */
	public Document newDocument( File file ) throws IOException, NotDocumentException
	{
		Objects.requireNonNull( file, "file is not null" );
		InputSource inputSource = new InputSource( file.toURI().toASCIIString() );
		return newDocument( inputSource );
	}

	/**
	 * Parse the XML located at the given URI into a {@link Document}. The XML document must be either a DDI document
	 * or an OAI-PMH document with a valid DDI document in the {@code <metadata>} element.
	 *
	 * @param uri the URI where the XML to be parsed is located.
	 * @throws NotDocumentException  if the XML is not well-formed.
	 * @throws IOException           if an IO error occurs.
	 * @implNote this calls {@link #newDocument(URL)} using {@link URI#toURL()} to transform the URI to a URL.
	 */
	public Document newDocument( URI uri ) throws IOException, NotDocumentException
	{
		Objects.requireNonNull( uri, "uri is not null" );
		InputSource inputSource = new InputSource( uri.toASCIIString() );
		return newDocument( inputSource );
	}

	/**
	 * Parse the XML located at the given URL into a {@link Document}. The XML document must be either a DDI document
	 * or an OAI-PMH document with a valid DDI document in the {@code <metadata>} element.
	 *
	 * @param url the URL where the XML to be parsed is located.
	 * @throws NotDocumentException if the XML is not well-formed.
	 * @throws IOException          if an IO error occurs.
	 */
	public Document newDocument( URL url ) throws IOException, NotDocumentException
	{
		Objects.requireNonNull( url, "url is null" );
		InputSource inputSource = new InputSource( url.toExternalForm() );
		return newDocument( inputSource );
	}

	/**
	 * Parse the XML located at the given URL into a {@link Document}. The XML document must be either a DDI document
	 * or an OAI-PMH document with a valid DDI document in the {@code <metadata>} element.
	 *
	 * @param inputStream the input stream of the XML to be parsed.
	 * @throws NotDocumentException if the XML is not well-formed.
	 * @throws IOException          if an IO error occurs.
	 */
	public Document newDocument( InputStream inputStream ) throws IOException, NotDocumentException
	{
		Objects.requireNonNull( inputStream, "inputStream is null" );
		return newDocument( new InputSource( inputStream ) );
	}

	/**
	 * Returns a validation gate with the given name.
	 */
	public static ValidationGate getValidationGate( ValidationGateName name )
	{
		requireNonNull( name, "name must not be null" );
		return name.getValidationGate();
	}

	public Profile newProfile( URI uri ) throws NotDocumentException
	{
		Objects.requireNonNull( uri, "uri must not be null" );
		InputSource inputSource = new InputSource( uri.toASCIIString() );
		return newProfile( inputSource );
	}

	public Profile newProfile( URL url ) throws NotDocumentException
	{
		Objects.requireNonNull( url, "url must not be null" );
		InputSource inputSource = new InputSource( url.toExternalForm() );
		return newProfile( inputSource );
	}

	public Profile newProfile( File file ) throws NotDocumentException
	{
		Objects.requireNonNull( file, "file must not be null" );
		InputSource inputSource = new InputSource( file.toURI().toASCIIString() );
		return newProfile( inputSource );
	}

	public Profile newProfile( InputStream inputStream ) throws NotDocumentException
	{
		Objects.requireNonNull( inputStream, "inputStream must not be null" );
		InputSource inputSource = new InputSource( inputStream );
		return newProfile( inputSource );
	}

	private Profile newProfile( InputSource inputSource ) throws NotDocumentException
	{
		try
		{
			XMLDocument document = XMLDocument.newBuilder().build(inputSource);
			return new DomSemiStructuredDdiProfile( document );
		}
		catch ( SAXException | IOException e )
		{
			throw new NotDocumentException( e );
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
	 * @param inputSource the input source of the XML to be parsed.
	 * @throws NotDocumentException if the XML is not well-formed.
	 * @throws IOException          if an IO error occurs.
	 */
	public Document newDocument( InputSource inputSource ) throws IOException, NotDocumentException
	{
		Objects.requireNonNull( inputSource, "inputSource is null" );

		try
		{
			// Parse the XML document
			XMLDocument document = XMLDocument.newBuilder().locationInfoAware( true ).namespaceAware( true ).build( inputSource );
			String namespace = document.getNamespace();

			if ( OAI_PMH_XML_NAMESPACE.equals( namespace ))
			{
				// Try to extract <metadata> and recurse
				document.setRootElement( "/oai:OAI-PMH/oai:GetRecord/oai:record/oai:metadata/*", "oai", OAI_PMH_XML_NAMESPACE );
			}

			return new DomCodebookDocument( document, cvrMap );
		}
		catch ( XmlNotWellformedException | XPathExpressionException | SAXException e )
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
