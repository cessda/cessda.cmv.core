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

import static org.gesis.commons.resource.Resource.newResource;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;
import org.gesis.commons.resource.TextResource;

@XmlType( name = ValidationRequestV0.DOCUMENT_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class DocumentV0
{
	@XmlElement
	private URI uri;

	@XmlCDATA
	private String content;

	public URI getUri()
	{
		return uri;
	}

	public void setUri( URI uri )
	{
		this.uri = uri;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent( String content )
	{
		this.content = content;
	}

	public List<String> validate()
	{
		List<String> messages = new ArrayList<>();
		requireNotBothNull().ifPresent( messages::add );
		requireConsistency().ifPresent( messages::add );
		return messages;
	}

	private Optional<String> requireNotBothNull()
	{
		if ( uri == null && content == null )
		{
			return Optional.of( "Uri and content are both missing" );
		}
		else
		{
			return Optional.empty();
		}
	}

	private Optional<String> requireConsistency()
	{
		if ( (uri != null && content != null) && !content.equals( new TextResource( newResource( uri ) ).toString() ) )
		{
			return Optional.of( "Content differs from uri's content" );
		}
		else
		{
			return Optional.empty();
		}
	}
}
