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

import org.gesis.commons.resource.Resource;
import org.gesis.commons.resource.ResourceLabelProvider;
import org.gesis.commons.xml.DomDocument;
import org.gesis.commons.xml.XercesXalanDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

public class ProfileResourceLabelProvider extends ResourceLabelProvider
{
	private static final Logger LOGGER = LoggerFactory.getLogger( ProfileResourceLabelProvider.class );

	@Override
	public String getLabel( Resource resource )
	{
		requireNonNull( resource );
		try
		{
			DomDocument.V14 document = XercesXalanDocument.newBuilder()
					.ofInputStream( resource.readInputStream() )
					.namespaceUnaware()
					.build();
			String label = document.selectNode( "/DDIProfile/DDIProfileName/String" ).getTextContent() +
					" " +
					document.selectNode( "/DDIProfile/Version" ).getTextContent();
			return label;
		}
		catch (Exception e)
		{
			LOGGER.warn( e.getMessage(), e );
			return super.getLabel( resource );
		}
	}
}
