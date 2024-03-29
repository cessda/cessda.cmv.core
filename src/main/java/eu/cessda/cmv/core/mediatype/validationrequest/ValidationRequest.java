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
package eu.cessda.cmv.core.mediatype.validationrequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.cessda.cmv.core.ValidationGateName;
import org.gesis.commons.xml.jaxb.DefaultNamespacePrefixMapper;
import org.gesis.commons.xml.jaxb.JaxbDocument;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.*;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.NoSuchFileException;
import java.util.*;

@XmlRootElement( name = ValidationRequest.VALIDATIONREQUEST_ELEMENT )
@XmlType( name = ValidationRequest.VALIDATIONREQUEST_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class ValidationRequest extends JaxbDocument
{
	static final String MAJOR = "0";
	static final String MINOR = "2";
	static final String VERSION = MAJOR + "." + MINOR;
	public static final String MEDIATYPE = "application/vnd.eu.cessda.cmv.core.mediatype.validation-request.v" + VERSION
			+ "+xml";
	static final String SCHEMALOCATION_HOST = "https://raw.githubusercontent.com/cessda/cessda.cmv.core/stable/schema";
	public static final String SCHEMALOCATION_FILENAME = "validation-request-v" + VERSION + ".xsd";

	static final String NAMESPACE_DEFAULT_PREFIX = "";
	static final String NAMESPACE_DEFAULT_URI = "cmv:validation-request:v" + MAJOR;
	static final String SCHEMALOCATION_URI = SCHEMALOCATION_HOST + "/" + SCHEMALOCATION_FILENAME;
	static final String SCHEMALOCATION = NAMESPACE_DEFAULT_URI + " " + SCHEMALOCATION_URI;
	static final String VALIDATIONREQUEST_ELEMENT = "ValidationRequest";
	static final String VALIDATIONREQUEST_TYPE = VALIDATIONREQUEST_ELEMENT + "ValidationRequestType";
	static final String DOCUMENT_TYPE = "DocumentType";
	static final String VALIDATIONGATENAME_TYPE = "ValidationGateNameType";

	private static final JAXBContext JAXBCONTEXT = newJaxbContext( ValidationRequest.class );

	@Valid
	@NotNull
	@XmlElement( required = true )
	private Document document;

	@Valid
	@NotNull
	@XmlElement( required = true )
	private Document profile;

	@XmlElement
	private ValidationGateName validationGateName;

	@JsonProperty( "constraints" )
	@XmlElement( name = "Constraint" )
	@XmlElementWrapper( name = "Constraints" )
	private List<String> constraints;

	public ValidationRequest()
	{
		super( SCHEMALOCATION, new DefaultNamespacePrefixMapper( NAMESPACE_DEFAULT_URI ) );
	}

	public Document getDocument()
	{
		return document;
	}

	public void setDocument( String content )
	{
		this.document = new ContentDocument( content );
	}

	public void setDocument( URI uri )
	{
		this.document = new UriDocument( uri );
	}

	public Document getProfile()
	{
		return profile;
	}

	public void setProfile( String content )
	{
		this.profile = new ContentDocument( content );
	}

	public void setProfile( URI uri )
	{
		this.profile = new UriDocument( uri );
	}

	public ValidationGateName getValidationGateName()
	{
		return validationGateName;
	}

	public void setValidationGateName( ValidationGateName validationGateName )
	{
		this.validationGateName = validationGateName;
	}

	public List<String> getConstraints()
	{
		if ( constraints == null )
		{
			return Collections.emptyList();
		}
		else
		{
			return Collections.unmodifiableList( constraints );
		}
	}

	public void setConstraints( Collection<String> constraints )
	{
		this.constraints = new ArrayList<>( constraints );
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
		if ( validationGateName == null && ( constraints == null || constraints.isEmpty() ) )
		{
			messages.add( "Validation gate or constraints is missing" );
		}
		return messages;
	}

	public static ValidationRequest read( InputStream inputStream )
	{
		return JaxbDocument.read( inputStream, JAXBCONTEXT );
	}

	public static ValidationRequest open( File file ) throws NoSuchFileException
	{
		return JaxbDocument.open( file, JAXBCONTEXT );
	}

	public static ValidationRequest read( String string )
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
	public boolean equals( Object o )
	{
		if ( this == o ) return true;
		if ( o == null || getClass() != o.getClass() ) return false;
		ValidationRequest that = (ValidationRequest) o;
		return Objects.equals( document, that.document ) && Objects.equals( profile, that.profile ) &&
			validationGateName == that.validationGateName && Objects.equals( constraints, that.constraints );
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( document, profile, validationGateName, constraints );
	}

	@Override
	public String toString()
	{
		return toString( JAXBCONTEXT );
	}
}
