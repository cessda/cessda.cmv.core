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
package eu.cessda.cmv.core.mediatype.validationreport.v0;

import eu.cessda.cmv.core.ValidationReport;
import org.gesis.commons.xml.jaxb.DefaultNamespacePrefixMapper;
import org.gesis.commons.xml.jaxb.JaxbDocument;
import org.gesis.commons.xml.jaxb.NamespacePrefixMapper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.*;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlRootElement( name = ValidationReportV0.VALIDATIONREPORT_ELEMENT )
@XmlType( name = ValidationReportV0.VALIDATIONREPORT_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class ValidationReportV0 extends JaxbDocument
		implements ValidationReport
{
	static final String MAJOR = "0";
	static final String MINOR = "1";
	static final String VERSION = MAJOR + "." + MINOR;
	public static final String MEDIATYPE = "application/vnd.eu.cessda.cmv.core.mediatype.validation-report.v" + VERSION
			+ "+xml";
	static final String SCHEMALOCATION_HOST = "https://raw.githubusercontent.com/cessda/cessda.cmv.core/stable/schema";
	public static final String SCHEMALOCATION_FILENAME = "validation-report-v" + VERSION + ".xsd";

	static final String NAMESPACE_DEFAULT_PREFIX = "";
	static final String NAMESPACE_DEFAULT_URI = "cmv:validation-report:v" + MAJOR;
	static final String SCHEMALOCATION_URI = SCHEMALOCATION_HOST + "/" + SCHEMALOCATION_FILENAME;
	static final String SCHEMALOCATION = NAMESPACE_DEFAULT_URI + " " + SCHEMALOCATION_URI;

	static final String VALIDATIONREPORT_ELEMENT = "ValidationReport";
	static final String VALIDATIONREPORT_TYPE = VALIDATIONREPORT_ELEMENT + "Type";
	static final String DOCUMENTURI_ELEMENT = "DocumentUri";
	static final String CONSTRAINTVIOLATION_ELEMENT = "ConstraintViolation";
	static final String CONSTRAINTVIOLATION_TYPE = CONSTRAINTVIOLATION_ELEMENT + "Type";
	static final String LOCATIONINFO_ELEMENT = "LocationInfo";
	static final String LOCATIONINFO_TYPE = LOCATIONINFO_ELEMENT + "Type";

	private static final JAXBContext JAXBCONTEXT = newJaxbContext( ValidationReportV0.class );

	@XmlElement( name = DOCUMENTURI_ELEMENT )
	private URI documentUri;

	@XmlElement( name = CONSTRAINTVIOLATION_ELEMENT )
	private List<ConstraintViolationV0> constraintViolations;

	protected ValidationReportV0( String schemaLocation, NamespacePrefixMapper namespacePrefixMapper )
	{
		super( schemaLocation, namespacePrefixMapper );
	}

	public ValidationReportV0()
	{
		super( SCHEMALOCATION, new DefaultNamespacePrefixMapper( NAMESPACE_DEFAULT_URI ) );
		constraintViolations = new ArrayList<>();
	}

	public List<ConstraintViolationV0> getConstraintViolations()
	{
		return constraintViolations;
	}

	public void setConstraintViolations( List<ConstraintViolationV0> constraintViolations )
	{
		this.constraintViolations = constraintViolations;
	}

	public URI getDocumentUri()
	{
		return documentUri;
	}

	public void setDocumentUri( URI documentUri )
	{
		this.documentUri = documentUri;
	}

	public static ValidationReportV0 read( InputStream inputStream )
	{
		return JaxbDocument.read( inputStream, JAXBCONTEXT );
	}

	public static ValidationReportV0 open( File file ) throws NoSuchFileException
	{
		return JaxbDocument.open( file, JAXBCONTEXT );
	}

	public static ValidationReportV0 read( String string )
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
	public String toString()
	{
		return toString( JAXBCONTEXT );
	}

	@Override
	public boolean equals( Object o )
	{
		if ( this == o ) return true;
		if ( o == null || getClass() != o.getClass() ) return false;
		ValidationReportV0 that = (ValidationReportV0) o;
		return Objects.equals( documentUri, that.documentUri ) &&
			Objects.equals( constraintViolations, that.constraintViolations );
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( super.hashCode(), documentUri, constraintViolations );
	}
}
