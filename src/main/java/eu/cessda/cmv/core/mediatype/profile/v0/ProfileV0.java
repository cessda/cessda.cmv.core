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
package eu.cessda.cmv.core.mediatype.profile.v0;

import org.gesis.commons.xml.jaxb.DefaultNamespacePrefixMapper;
import org.gesis.commons.xml.jaxb.JaxbDocument;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.*;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement( name = ProfileV0.JAXB_ELEMENT )
@XmlType( name = ProfileV0.JAXB_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class ProfileV0 extends JaxbDocument
{
	static final String MAJOR = "0";
	static final String MINOR = "1";
	static final String VERSION = MAJOR + "." + MINOR;
	public static final String MEDIATYPE = "application/vnd.eu.cessda.cmv.core.mediatype.profile.v" + VERSION + "+xml";
	static final String SCHEMALOCATION_HOST = "https://raw.githubusercontent.com/cessda/cessda.cmv.core/stable/schema";
	public static final String SCHEMALOCATION_FILENAME = "profile-v" + VERSION + ".xsd";

	static final String NAMESPACE_DEFAULT_PREFIX = "";
	static final String NAMESPACE_DEFAULT_URI = "cmv:profile:v" + MAJOR;
	static final String SCHEMALOCATION_URI = SCHEMALOCATION_HOST + "/" + SCHEMALOCATION_FILENAME;
	static final String SCHEMALOCATION = NAMESPACE_DEFAULT_URI + " " + SCHEMALOCATION_URI;

	static final String JAXB_ELEMENT = "Profile";
	static final String JAXB_TYPE = JAXB_ELEMENT + "Type";

	private static final JAXBContext JAXBCONTEXT = newJaxbContext( ProfileV0.class );

	@XmlElement
	private String name;

	@XmlElements( {
			@XmlElement(
					name = MaximumElementOccuranceConstraintV0.JAXB_ELEMENT,
					type = MaximumElementOccuranceConstraintV0.class ),
			@XmlElement(
					name = MandatoryNodeConstraintV0.JAXB_ELEMENT,
					type = MandatoryNodeConstraintV0.class ),
			@XmlElement(
					name = OptionalNodeConstraintV0.JAXB_ELEMENT,
					type = OptionalNodeConstraintV0.class ),
			@XmlElement(
					name = RecommendedNodeConstraintV0.JAXB_ELEMENT,
					type = RecommendedNodeConstraintV0.class ),
			@XmlElement(
					name = PredicatelessXPathConstraintV0.JAXB_ELEMENT,
					type = PredicatelessXPathConstraintV0.class ),
			@XmlElement(
					name = CompilableXPathConstraintV0.JAXB_ELEMENT,
					type = CompilableXPathConstraintV0.class )
	} )
	private List<ConstraintV0> constraints;

	public ProfileV0()
	{
		super( ProfileV0.SCHEMALOCATION, new DefaultNamespacePrefixMapper( NAMESPACE_DEFAULT_URI ) );
		constraints = new ArrayList<>();
	}

	public String getName()
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public List<ConstraintV0> getConstraints()
	{
		return constraints;
	}

	public void setConstraints( List<ConstraintV0> constraints )
	{
		this.constraints = constraints;
	}

	public static ProfileV0 read( InputStream inputStream )
	{
		return JaxbDocument.read( inputStream, JAXBCONTEXT );
	}

	public static ProfileV0 open( File file ) throws NoSuchFileException
	{
		return JaxbDocument.open( file, JAXBCONTEXT );
	}

	public static ProfileV0 read( String string )
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
}
