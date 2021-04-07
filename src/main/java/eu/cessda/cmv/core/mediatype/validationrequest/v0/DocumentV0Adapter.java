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

import java.net.URI;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

class DocumentV0Adapter extends XmlAdapter<DocumentV0Adapter.AdaptedDocument, DocumentV0>
{
	@Override
	public AdaptedDocument marshal( DocumentV0 document )
			throws Exception
	{
		if ( null == document )
		{
			return null;
		}
		AdaptedDocument adaptedDocument = new AdaptedDocument();
		if ( document instanceof UriDocumentV0 )
		{
			adaptedDocument.uri = ((UriDocumentV0) document).getUri();
		}
		else
		{
			adaptedDocument.content = ((ContentDocumentV0) document).getContent();
		}
		return adaptedDocument;
	}

	@Override
	public DocumentV0 unmarshal( AdaptedDocument adaptedContactMethod )
			throws Exception
	{
		if ( null == adaptedContactMethod )
		{
			return null;
		}
		if ( null != adaptedContactMethod.uri )
		{
			return new UriDocumentV0( adaptedContactMethod.uri );
		}
		else
		{
			return new ContentDocumentV0( adaptedContactMethod.content );
		}
	}

	public static class AdaptedDocument
	{
		@XmlElement
		public URI uri;

		@XmlCDATA
		public String content;
	}
}
