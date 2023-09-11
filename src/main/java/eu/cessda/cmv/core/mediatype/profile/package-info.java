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
@XmlSchema(
		namespace = Profile.NAMESPACE_DEFAULT_URI,
		xmlns = { @XmlNs( prefix = Profile.NAMESPACE_DEFAULT_PREFIX, namespaceURI = Profile.NAMESPACE_DEFAULT_URI ),
				@XmlNs( prefix = "xsi", namespaceURI = W3C_XML_SCHEMA_INSTANCE_NS_URI ) },
		elementFormDefault = XmlNsForm.QUALIFIED )
@XmlNameTransformer( UpperCamelCaseNameTransformer.class )
package eu.cessda.cmv.core.mediatype.profile;

import org.eclipse.persistence.oxm.annotations.XmlNameTransformer;
import org.gesis.commons.xml.jaxb.moxy.UpperCamelCaseNameTransformer;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI;
