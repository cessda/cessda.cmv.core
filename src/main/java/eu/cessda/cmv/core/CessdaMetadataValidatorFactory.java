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
import eu.cessda.cmv.core.mediatype.validationreport.ValidationReport;
import org.gesis.commons.xml.NoSuchNodeException;
import org.gesis.commons.xml.XMLDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

public class CessdaMetadataValidatorFactory implements ValidationService
{
	private static final Logger log = LoggerFactory.getLogger( CessdaMetadataValidatorFactory.class );

	private static final String URI_CONVERSION_FAILED = "Couldn't convert {} into a URI: {}";

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
		return newDocument( file.toURI(), inputSource );
	}

	/**
	 * Parse the XML at the given path into a {@link Document}. The XML document must be either a DDI document
	 * or an OAI-PMH document with a valid DDI document in the {@code <metadata>} element.
	 *
	 * @param path the path where the XML to be parsed is located.
	 * @throws NotDocumentException if the XML is not well-formed.
	 * @throws IOException          if an IO error occurs.
	 */
	public Document newDocument( Path path ) throws IOException, NotDocumentException
	{
		Objects.requireNonNull( path, "path is not null" );
		InputSource inputSource = new InputSource( path.toUri().toASCIIString() );
		return newDocument( path.toUri(), inputSource );
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
		return newDocument( uri, inputSource );
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

		URI uri = null;
		try
		{
			uri = url.toURI();
			return newDocument( uri );
		}
		catch ( URISyntaxException e )
		{
			log.warn( URI_CONVERSION_FAILED, url, e.getMessage() );
		}

		InputSource inputSource = new InputSource( url.toExternalForm() );
        return newDocument( uri, inputSource );
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
	 * Parse the XML contained in the request to a {@link Document}. The XML document must be either a DDI document
	 * or an OAI-PMH document with a valid DDI document in the {@code <metadata>} element.
	 *
	 * @param document the request document.
	 * @throws NotDocumentException if the XML is not well-formed.
	 * @throws IOException          if an IO error occurs.
	 */
	public Document newDocument( eu.cessda.cmv.core.mediatype.validationrequest.Document document ) throws IOException, NotDocumentException
	{
		Objects.requireNonNull( document, "document is null" );
		return newDocument( document.toInputSource() );
	}

	/**
	 * Returns a validation gate with the given name.
	 */
	public static ValidationGate getValidationGate( ValidationGateName name )
	{
		requireNonNull( name, "name must not be null" );
		return name.getValidationGate();
	}

	/**
	 * Parse the XML located at the given URI into a {@link Profile}. The XML document must be a valid DDI profile.
	 *
	 * @param uri the URI of the XML to parse.
	 * @throws NotDocumentException if the XML is not well-formed, or no valid constraints were found.
	 */
	public Profile newProfile( URI uri ) throws NotDocumentException, IOException
	{
		Objects.requireNonNull( uri, "uri must not be null" );
		InputSource inputSource = new InputSource( uri.toASCIIString() );
		return newProfile( inputSource );
	}

	/**
	 * Parse the XML located at the given URL into a {@link Profile}. The XML document must be a valid DDI profile.
	 *
	 * @param url the URL of the XML to parse.
	 * @throws NotDocumentException if the XML is not well-formed, or no valid constraints were found.
	 */
	public Profile newProfile( URL url ) throws NotDocumentException, IOException
	{
		Objects.requireNonNull( url, "url must not be null" );
		InputSource inputSource = new InputSource( url.toExternalForm() );
		return newProfile( inputSource );
	}

	/**
	 * Parse the XML located at the given file into a {@link Profile}. The XML document must be a valid	DDI profile.
	 *
	 * @param file the file where the XML to be parsed is located.
	 * @throws NotDocumentException if the XML is not well-formed, or no valid constraints were found.
	 */
	public Profile newProfile( File file ) throws NotDocumentException, IOException
	{
		Objects.requireNonNull( file, "file must not be null" );
		InputSource inputSource = new InputSource( file.toURI().toASCIIString() );
		return newProfile( inputSource );
	}

	/**
	 * Parse the XML located at the given path into a {@link Profile}. The XML document must be a valid	DDI profile.
	 *
	 * @param path the path where the XML to be parsed is located.
	 * @throws NotDocumentException if the XML is not well-formed, or no valid constraints were found.
	 */
	public Profile newProfile( Path path ) throws NotDocumentException, IOException
	{
		Objects.requireNonNull( path, "path must not be null" );
		InputSource inputSource = new InputSource( path.toUri().toASCIIString() );
		return newProfile( inputSource );
	}

	/**
	 * Parse the XML represented by the given input stream into a {@link Profile}.
	 * The XML document must be a valid DDI profile.
	 *
	 * @param inputStream the input stream of the XML to be parsed.
	 * @throws NotDocumentException if the XML is not well-formed, or no valid constraints were found.
	 */
	public Profile newProfile( InputStream inputStream ) throws NotDocumentException, IOException
	{
		Objects.requireNonNull( inputStream, "inputStream must not be null" );
		InputSource inputSource = new InputSource( inputStream );
		return newProfile( inputSource );
	}

	/**
	 * Parse the XML represented by the given request into a {@link Profile}.
	 * The XML document must be a valid DDI profile.
	 *
	 * @param profile the request containing the XML to be parsed.
	 * @throws NotDocumentException if the XML is not well-formed, or no valid constraints were found.
	 */
	public Profile newProfile( eu.cessda.cmv.core.mediatype.validationrequest.Document profile ) throws NotDocumentException, IOException
	{
		Objects.requireNonNull( profile, "profile must not be null" );
		return newProfile( profile.toInputSource() );
	}

	/**
	 * Parse the XML represented by the given input source into a {@link Profile}.
	 * The XML document must be a valid DDI profile.
	 *
	 * @param inputSource the input source of the XML to be parsed.
	 * @throws NotDocumentException if the XML is not well-formed, or no valid constraints were found.
	 */
	public Profile newProfile( InputSource inputSource ) throws NotDocumentException, IOException
	{
		try
		{
			XMLDocument document = XMLDocument.newBuilder().build(inputSource);
			document.setNamespaceContext( DomSemiStructuredDdiProfile.getProfileNamespaceContext() );
			return new DomSemiStructuredDdiProfile( document );
		}
		catch ( SAXException e )
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
		URI uri = null;
		if (inputSource.getSystemId() != null)
		{
			try
			{
				uri = new URI( inputSource.getSystemId() );
			}
			catch ( URISyntaxException e )
			{
				log.warn( URI_CONVERSION_FAILED, inputSource.getSystemId(), e.getMessage() );
			}
		}

		return newDocument( uri, inputSource );
	}

	private Document newDocument( URI uri, InputSource inputSource ) throws IOException, NotDocumentException
	{
		Objects.requireNonNull( inputSource, "inputSource is null" );

		try
		{
			// Parse the XML document
			XMLDocument document = XMLDocument.newBuilder().locationInfoAware( true ).build( inputSource );
			String namespace = document.getNamespace();

			if ( OAI_PMH_XML_NAMESPACE.equals( namespace ))
			{
				// Try to extract <metadata> and recurse
				NamespaceContextImpl namespaceContext = new NamespaceContextImpl( Collections.singletonMap( "oai", OAI_PMH_XML_NAMESPACE ) );
				document.setRootElement( "/oai:OAI-PMH/oai:GetRecord/oai:record/oai:metadata/*", namespaceContext );
			}

			return new DomCodebookDocument( uri, document, cvrMap );
		}
		catch ( NoSuchNodeException  | SAXException e )
		{
			throw new NotDocumentException( e );
		}
		catch ( XPathExpressionException e )
		{
			/*
			 XPathExpressionException should never be thrown as the
			 XPath used to set the root element will always be valid
			*/
			throw new IllegalStateException( e );
		}
	}

	/**
	 * Returns a validation gate with the given name.
	 */
	ValidationGate newValidationGate( ValidationGateName name )
	{
		return getValidationGate( name );
	}

	/**
	 * Validates the document against the given profile using the specified validation gate.
	 *
	 * @param documentUri the URI of the document to validate.
	 * @param profileUri the URI of the profile to validate the document against.
	 * @param validationGate the validation gate to use.
	 * @return the validation report.
	 */
	@Override
	public ValidationReport validate( URI documentUri, URI profileUri, ValidationGate validationGate ) throws IOException, NotDocumentException
	{
		Document document = newDocument( documentUri );
		Profile profile = newProfile( profileUri );

		return validate( document, profile, validationGate );
	}

	/**
	 * Validates the document against the given profile using the specified validation gate.
	 *
	 * @param document the document to validate.
	 * @param profile the profile to validate the document against.
	 * @param validationGate the validation gate to use.
	 * @return the validation report.
	 */
	@Override
	public ValidationReport validate( Document document, Profile profile, ValidationGate validationGate )
	{
		List<eu.cessda.cmv.core.ConstraintViolation> constraintViolations = validationGate.validate( document, profile );

		// Transform all constraint violations into the media-type form
		List<eu.cessda.cmv.core.mediatype.validationreport.ConstraintViolation> mediaTypeConstraintViolations = new ArrayList<>( constraintViolations.size() );
		for ( eu.cessda.cmv.core.ConstraintViolation constraintViolation : constraintViolations )
		{
			eu.cessda.cmv.core.mediatype.validationreport.ConstraintViolation mediaTypeViolation = new eu.cessda.cmv.core.mediatype.validationreport.ConstraintViolation( constraintViolation );
			mediaTypeConstraintViolations.add( mediaTypeViolation );
		}

		// Create the validation report
		return new ValidationReport( document.getURI(), mediaTypeConstraintViolations );
	}

	/**
	 * Split an OAI-PMH ListRecords response into individual documents.
	 * <p>
	 * The URI of the documents will be {@link InputSource#getSystemId()}#oai-pmh-id
	 *
	 * @param inputSource the source document to be split.
	 * @return a list of split documents.
	 * @throws IOException if an IO error occurs.
	 * @throws NotDocumentException if the document is not an OAI-PMH ListRecords response or if the XML is not well-formed.
	 */
	public List<Document> splitListRecordsResponse( InputSource inputSource ) throws IOException, NotDocumentException
	{
		// Split the document into constituent records
		try
		{
			// Parse the input source
			XMLDocument listRecordsResponse = XMLDocument.newBuilder().locationInfoAware( true ).build( inputSource );
			listRecordsResponse.setNamespaceContext( new NamespaceContextImpl( Collections.singletonMap( "oai", OAI_PMH_XML_NAMESPACE ) ) );

			// Determine if this is a list records response
			Node node = listRecordsResponse.selectNode( "/oai:OAI-PMH/oai:request" );
			if (node == null)
			{
				throw new NotDocumentException("Document is not an OAI-PMH response");
			}

			if (!"ListRecords".equals(((Element) node).getAttribute( "verb" )))
			{
				throw new NotDocumentException("OAI-PMH response is not a ListRecords response");
			}

			List<Node> recordNodeList = listRecordsResponse.selectNodes( "/oai:OAI-PMH/oai:ListRecords/oai:record" );
			List<Document> documentList = new ArrayList<>(recordNodeList.size());
			for ( Node recordNode : recordNodeList)
			{
				Node metadataNode = listRecordsResponse.selectNode( recordNode, "oai:metadata/*" );
				if ( metadataNode != null )
				{
					// Get document identifier
					Node identifierElement = listRecordsResponse.selectNode( recordNode, "oai:header/oai:identifier" );
					String identifier = identifierElement.getTextContent();

					// Create document from the metadata node
					XMLDocument documentFromElement = XMLDocument.createDocumentFromElement( (Element) metadataNode );

					// Convert to Document
					Document doc = new DomCodebookDocument( URI.create( inputSource.getSystemId() + '#' + identifier ), documentFromElement );
					documentList.add( doc );
				}
			}

			// Return
			return documentList;
		}
		catch ( SAXException e )
		{
			throw new NotDocumentException( e );
		}
		catch ( XPathExpressionException e )
		{
			throw new IllegalStateException( e );
		}
	}

	/**
	 * Returns this as a validation service for compatibility with previous versions of CMV
	 */
	public ValidationService newValidationService()
	{
		return this;
	}
}
