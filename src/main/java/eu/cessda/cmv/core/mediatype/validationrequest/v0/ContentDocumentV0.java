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
import org.eclipse.persistence.oxm.annotations.XmlCDATA;
import org.gesis.commons.resource.Resource;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.gesis.commons.resource.Resource.newResource;

public class ContentDocumentV0 extends DocumentV0
{
	@NotNull
	@Schema( description = "DDI document", required = true )
	@XmlElement( required = true )
	@XmlCDATA
	private String content;

	ContentDocumentV0()
	{
	}

	ContentDocumentV0( String content )
	{
		this.content = content;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent( String content )
	{
		this.content = content;
	}

	@Override
	public Resource toResource()
	{
		return newResource( new ByteArrayInputStream( content.getBytes( StandardCharsets.UTF_8 ) ) );
	}
}
