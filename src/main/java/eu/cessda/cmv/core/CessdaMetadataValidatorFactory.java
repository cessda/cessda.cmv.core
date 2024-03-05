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
import org.apache.tika.Tika;
import org.apache.tika.detect.CompositeDetector;
import org.gesis.commons.resource.io.DdiDetector;
import org.gesis.commons.resource.io.DdiInputStream;
import org.gesis.commons.resource.io.OaipmhV20Detector;
import org.gesis.commons.xml.SimpleNamespaceContext;
import org.gesis.commons.xml.XMLDocument;
import org.gesis.commons.xml.XmlNotWellformedException;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.*;
import java.net.MalformedURLException;
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

    private final Tika documentMediaTypeDetector = new Tika( new CompositeDetector( new DdiDetector(), new OaipmhV20Detector() ) );
	private final ConcurrentHashMap<URI, ControlledVocabularyRepository> cvrMap = new ConcurrentHashMap<>();

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
	 * Returns a validation gate with the given name.
	 */
	public static ValidationGate getValidationGate( ValidationGateName name )
	{
		requireNonNull( name, "name must not be null" );
		return name.getValidationGate();
	}

	public Profile newProfile( URI uri ) throws IOException, NotDocumentException
	{
		Objects.requireNonNull( uri, "uri must not be null" );
		XMLDocument.Builder documentBuilder = XMLDocument.newBuilder().source( uri );
		return newProfile( documentBuilder );
	}

	public Profile newProfile( URL url ) throws IOException, NotDocumentException
	{
		Objects.requireNonNull( url, "url must not be null" );
		XMLDocument.Builder documentBuilder = XMLDocument.newBuilder().source( url );
		return newProfile( documentBuilder );
	}

	public Profile newProfile( File file ) throws IOException, NotDocumentException
	{
		Objects.requireNonNull( file, "file must not be null" );
		XMLDocument.Builder documentBuilder = XMLDocument.newBuilder().source( file );
		return newProfile( documentBuilder );
	}

	public Profile newProfile( InputStream inputStream ) throws IOException, NotDocumentException
	{
		Objects.requireNonNull( inputStream, "inputStream must not be null" );
		XMLDocument.Builder documentBuilder = XMLDocument.newBuilder().source( new DdiInputStream( inputStream ) );
		return newProfile( documentBuilder );
	}

	private Profile newProfile( XMLDocument.Builder documentBuilder ) throws NotDocumentException
	{
		try
		{
			XMLDocument document = documentBuilder.build();
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
				return new DomCodebookDocument( XMLDocument.newBuilder().locationInfoAware(true).namespaceAware().source( inputStream ).build(), cvrMap );
			}
			else if ( OaipmhV20Detector.MEDIATYPE.equals( mediaType ) )
			{
				// Try to extract <metadata> and recurse
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				documentBuilderFactory.setAttribute( XMLConstants.ACCESS_EXTERNAL_DTD, "" );
				documentBuilderFactory.setAttribute( XMLConstants.ACCESS_EXTERNAL_SCHEMA, "" );
				documentBuilderFactory.setNamespaceAware( true );
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				org.w3c.dom.Document sourceDocument = documentBuilder.parse( inputStream );

				SimpleNamespaceContext nsContext = new SimpleNamespaceContext();
				nsContext.bindNamespaceUri( "oai", "http://www.openarchives.org/OAI/2.0/" );
				XPath xpath = XPathFactory.newInstance().newXPath();
				xpath.setNamespaceContext( nsContext );
				XPathExpression metadataXPath = xpath.compile("/oai:OAI-PMH/oai:GetRecord/oai:record/oai:metadata/*" );
				NodeList metadataElements = (NodeList) metadataXPath.evaluate( sourceDocument, XPathConstants.NODESET );

				if (metadataElements.getLength() != 0)
				{
                    org.w3c.dom.Document targetDocument = documentBuilder.newDocument();
					targetDocument.appendChild( targetDocument.adoptNode( metadataElements.item( 0 ) ) );
					targetDocument.getDocumentElement().setAttribute( "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance" );
					return new DomCodebookDocument( XMLDocument.newBuilder().locationInfoAware(true).namespaceAware().source( targetDocument ).build(), cvrMap );
				}
			}

			// Media-type detection failed, or no metadata was present
			throw new NotDocumentException( mediaType );
		}
		catch ( XmlNotWellformedException | XPathExpressionException | ParserConfigurationException | SAXException e )
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
