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

import io.swagger.v3.oas.annotations.media.Schema;
import org.eclipse.persistence.oxm.annotations.XmlCDATA;
import org.xml.sax.InputSource;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.io.StringReader;
import java.util.Objects;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public class ContentDocument implements Document
{
	@NotNull
	@Schema( description = "DDI document", requiredMode = REQUIRED )
	@XmlElement( required = true )
	@XmlCDATA
	private String content;

	ContentDocument()
	{
	}

	ContentDocument( String content )
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
	public InputSource toInputSource()
	{
		StringReader reader = new StringReader( content );
		return new InputSource( reader );
	}

	@Override
	public boolean equals( Object o )
	{
		if ( this == o ) return true;
		if ( o == null || getClass() != o.getClass() ) return false;
		ContentDocument that = (ContentDocument) o;
		return Objects.equals( content, that.content );
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( content );
	}
}
