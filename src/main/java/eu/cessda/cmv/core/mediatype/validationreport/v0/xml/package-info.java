//@XmlSchema(
//		namespace = JaxbValidationReportV0.NAMESPACE_DEFAULT_URI,
//		elementFormDefault = QUALIFIED,
//		xmlns = {
//				@XmlNs(
//						prefix = JaxbValidationReportV0.NAMESPACE_DEFAULT_PREFIX,
//						namespaceURI = JaxbValidationReportV0.NAMESPACE_DEFAULT_URI ),
//				@XmlNs( prefix = "xsi", namespaceURI = W3C_XML_SCHEMA_INSTANCE_NS_URI ) } )
@XmlNameTransformer( UpperCamelCaseNameTransformer.class )
package eu.cessda.cmv.core.mediatype.validationreport.v0.xml;

import org.eclipse.persistence.oxm.annotations.XmlNameTransformer;
import org.gesis.commons.xml.jaxb.moxy.UpperCamelCaseNameTransformer;