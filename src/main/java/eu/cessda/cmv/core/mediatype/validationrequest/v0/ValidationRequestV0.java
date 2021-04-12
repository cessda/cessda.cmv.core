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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
	static final String VALIDATIONREQUEST_TYPE = VALIDATIONREQUEST_ELEMENT + "ValidationRequestType";
	static final String DOCUMENT_TYPE = "DocumentType";
	static final String VALIDATIONGATENAME_TYPE = "ValidationGateNameType";

	private static final JAXBContext JAXBCONTEXT = newJaxbContext( ValidationRequestV0.class );

	@Valid
	@NotNull
	@XmlElement( required = true )
	private DocumentV0 document;

	@Valid
	@NotNull
	@XmlElement( required = true )
	private DocumentV0 profile;

	@NotNull
	@XmlElement( required = true )
	private ValidationGateName validationGateName;

	protected ValidationRequestV0( String schemaLocation, NamespacePrefixMapper namespacePrefixMapper )
	{
		super( schemaLocation, namespacePrefixMapper );
	}

	public ValidationRequestV0()
	{
		super( SCHEMALOCATION, new DefaultNamespacePrefixMapper( NAMESPACE_DEFAULT_URI ) );
	}

	public DocumentV0 getDocument()
	{
		return document;
	}

	public void setDocument( String content )
	{
		this.document = new ContentDocumentV0( content );
	}

	public void setDocument( URI uri )
	{
		this.document = new UriDocumentV0( uri );
	}

	public DocumentV0 getProfile()
	{
		return profile;
	}

	public void setProfile( String content )
	{
		this.profile = new ContentDocumentV0( content );
	}

	public void setProfile( URI uri )
	{
		this.profile = new UriDocumentV0( uri );
	}

	public ValidationGateName getValidationGateName()
	{
		return validationGateName;
	}

	public void setValidationGateName( ValidationGateName validationGateName )
	{
		this.validationGateName = validationGateName;
	}

	public List<String> validate()
	{
		List<String> messages = new ArrayList<>();
		if ( document == null )
		{
			messages.add( "Document is missing" );
		}
		else
		{
			messages.addAll( document.validate() );
		}
		if ( profile == null )
		{
			messages.add( "Profile is missing" );
		}
		else
		{
			messages.addAll( profile.validate() );
		}
		if ( validationGateName == null )
		{
			messages.add( "Validation gate name is missing" );
		}
		return messages;
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
