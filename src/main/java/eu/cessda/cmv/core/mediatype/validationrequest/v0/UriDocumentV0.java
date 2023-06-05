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

import io.swagger.v3.oas.annotations.media.Schema;
import org.gesis.commons.resource.Resource;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.net.URI;
import java.util.Objects;

import static org.gesis.commons.resource.Resource.newResource;

public class UriDocumentV0 implements DocumentV0
{
	@NotNull
	@Schema( description = "Resolvable uri of remotely available resource", required = true )
	@XmlElement( required = true )
	private URI uri;

	UriDocumentV0()
	{
	}

	UriDocumentV0( URI uri )
	{
		this.uri = uri;
	}

	public URI getUri()
	{
		return uri;
	}

	public void setUri( URI uri )
	{
		this.uri = uri;
	}

	@Override
	public Resource toResource()
	{
		return newResource( uri );
	}

	@Override
	public boolean equals( Object o )
	{
		if ( this == o ) return true;
		if ( o == null || getClass() != o.getClass() ) return false;
		UriDocumentV0 that = (UriDocumentV0) o;
		return Objects.equals( uri, that.uri );
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( uri );
	}
}
