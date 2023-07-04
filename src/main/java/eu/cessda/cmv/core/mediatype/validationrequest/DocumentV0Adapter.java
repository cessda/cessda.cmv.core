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

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.net.URI;

class DocumentV0Adapter extends XmlAdapter<DocumentV0Adapter.AdaptedDocument, Document>
{
	@Override
	public AdaptedDocument marshal( Document document )
	{
		if ( null == document )
		{
			return null;
		}
		AdaptedDocument adaptedDocument = new AdaptedDocument();
		if ( document instanceof UriDocument )
		{
			adaptedDocument.uri = ((UriDocument) document).getUri();
		}
		else if ( document instanceof ContentDocument )
		{
			adaptedDocument.content = ((ContentDocument) document).getContent();
		}
		else
		{
			throw new UnsupportedOperationException(document.getClass().getSimpleName() + " cannot be marshalled");
		}
		return adaptedDocument;
	}

	@Override
	public Document unmarshal( AdaptedDocument adaptedContactMethod )
	{
		if ( null == adaptedContactMethod )
		{
			return null;
		}
		if ( null != adaptedContactMethod.uri )
		{
			return new UriDocument( adaptedContactMethod.uri );
		}
		else
		{
			return new ContentDocument( adaptedContactMethod.content );
		}
	}

	public static class AdaptedDocument
	{
		@NotNull
		@XmlElement( required = true )
		public URI uri;

		@NotNull
		@XmlElement( required = true )
		@XmlCDATA
		public String content;
	}
}
