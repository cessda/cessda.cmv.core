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
package eu.cessda.cmv.core.mediatype.validationreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;

@XmlType( name = ValidationReport.LOCATIONINFO_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class LocationInfo
{
	@XmlElement
	private int lineNumber;

	@XmlElement
	private int columnNumber;

	public LocationInfo()
	{
	}

	public LocationInfo( org.gesis.commons.xml.LocationInfo locationInfo )
	{
		this.lineNumber = locationInfo.getLineNumber();
		this.columnNumber = locationInfo.getColumnNumber();
	}

	public int getLineNumber()
	{
		return lineNumber;
	}

	public void setLineNumber( int lineNumber )
	{
		this.lineNumber = lineNumber;
	}

	public int getColumnNumber()
	{
		return columnNumber;
	}

	public void setColumnNumber( int columnNumber )
	{
		this.columnNumber = columnNumber;
	}

	@Override
	public String toString()
	{
		return "lineNumber: " + lineNumber + ", columnNumber: " + columnNumber;
	}

	@Override
	public boolean equals( Object o )
	{
		if ( this == o ) return true;
		if ( o == null || getClass() != o.getClass() ) return false;
		LocationInfo that = (LocationInfo) o;
		return lineNumber == that.lineNumber && columnNumber == that.columnNumber;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( lineNumber, columnNumber );
	}
}
