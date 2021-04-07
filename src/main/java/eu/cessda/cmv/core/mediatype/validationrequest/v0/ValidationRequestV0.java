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
package eu.cessda.cmv.core.mediatype.validationrequest.v0;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.NoSuchFileException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.TextResource;
import org.gesis.commons.xml.jaxb.DefaultNamespacePrefixMapper;
import org.gesis.commons.xml.jaxb.JaxbDocument;
import org.gesis.commons.xml.jaxb.NamespacePrefixMapper;

import eu.cessda.cmv.core.ValidationGateName;
import eu.cessda.cmv.core.ValidationRequest;

@XmlRootElement( name = ValidationRequestV0.VALIDATIONREQUEST_ELEMENT )
@XmlType( name = ValidationRequestV0.VALIDATIONREQUEST_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class ValidationRequestV0 extends JaxbDocument implements ValidationRequest
{
	static final String MAJOR = "0";
	static final String MINOR = "1";
	static final String VERSION = MAJOR + "." + MINOR;
	public static final String MEDIATYPE = "application/vnd.eu.cessda.cmv.core.mediatype.validation-request.v" + VERSION
			+ "+xml";
	static final String SCHEMALOCATION_HOST = "https://bitbucket.org/cessda/cessda.cmv.core/src/raw/stable/schema";
	public static final String SCHEMALOCATION_FILENAME = "validation-request-v" + VERSION + ".xsd";

	static final String NAMESPACE_DEFAULT_PREFIX = "";
	static final String NAMESPACE_DEFAULT_URI = "cmv:validation-request:v" + MAJOR;
	static final String SCHEMALOCATION_URI = SCHEMALOCATION_HOST + "/" + SCHEMALOCATION_FILENAME;
	static final String SCHEMALOCATION = NAMESPACE_DEFAULT_URI + " " + SCHEMALOCATION_URI;

	static final String VALIDATIONREQUEST_ELEMENT = "ValidationRequest";
	static final String VALIDATIONREQUEST_TYPE = VALIDATIONREQUEST_ELEMENT + "Type";
	static final String DOCUMENT_ELEMENT = "Document";
	static final String DOCUMENT_TYPE = DOCUMENT_ELEMENT + "Type";
	static final String PROFILE_ELEMENT = "Profile";
	static final String VALIDATIONGATENAME_ELEMENT = "ValidationGateName";
	static final String VALIDATIONGATENAME_TYPE = VALIDATIONGATENAME_ELEMENT + "Type";

	private static final JAXBContext JAXBCONTEXT = newJaxbContext( ValidationRequestV0.class );

	@XmlElement( name = DOCUMENT_ELEMENT )
	private DocumentV0 document;

	@XmlElement( name = PROFILE_ELEMENT )
	private DocumentV0 profile;

	@XmlElement( name = VALIDATIONGATENAME_ELEMENT )
	private ValidationGateName validationGateName;

	protected ValidationRequestV0( String schemaLocation, NamespacePrefixMapper namespacePrefixMapper )
	{
		super( schemaLocation, namespacePrefixMapper );
	}

	public ValidationRequestV0()
	{
		super( SCHEMALOCATION, new DefaultNamespacePrefixMapper( NAMESPACE_DEFAULT_URI ) );
	}

	public ValidationRequestV0( Resource profile, Resource document, ValidationGateName validationGateName )
	{
		this();
		setDocument( newDocument( document ) );
		setProfile( newDocument( profile ) );
		setValidationGateName( validationGateName );
	}

	private DocumentV0 newDocument( Resource resource )
	{
		requireNonNull( resource );
		DocumentV0 documentV0 = new DocumentV0();
		documentV0.setUri( resource.getUri() );
		documentV0.setContent( new TextResource( resource ).toString() );
		return documentV0;
	}

	public DocumentV0 getDocument()
	{
		return document;
	}

	public void setDocument( DocumentV0 document )
	{
		this.document = document;
	}

	public DocumentV0 getProfile()
	{
		return profile;
	}

	public void setProfile( DocumentV0 profile )
	{
		this.profile = profile;
	}

	public ValidationGateName getValidationGateName()
	{
		return validationGateName;
	}

	public void setValidationGateName( ValidationGateName validationGateName )
	{
		this.validationGateName = validationGateName;
	}

	public static ValidationRequestV0 read( InputStream inputStream )
	{
		return JaxbDocument.read( inputStream, JAXBCONTEXT );
	}

	public static ValidationRequestV0 open( File file ) throws NoSuchFileException
	{
		return JaxbDocument.open( file, JAXBCONTEXT );
	}

	public static ValidationRequestV0 read( String string )
	{
		return JaxbDocument.read( string, JAXBCONTEXT );
	}

	public static void generateSchema( File file )
	{
		JaxbDocument.generateSchema( file, JAXBCONTEXT );
	}

	@Override
	public void write( OutputStream outputStream )
	{
		write( outputStream, JAXBCONTEXT );
	}

	@Override
	public void saveAs( File file )
	{
		saveAs( file, JAXBCONTEXT );
	}

	@Override
	public String toString()
	{
		return toString( JAXBCONTEXT );
	}
}
