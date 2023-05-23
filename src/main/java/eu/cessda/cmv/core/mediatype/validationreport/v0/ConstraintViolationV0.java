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

import eu.cessda.cmv.core.ConstraintViolation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;

@XmlType( name = ValidationReportV0.CONSTRAINTVIOLATION_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class ConstraintViolationV0
{
	@XmlElement( required = true )
	private String message;

	@XmlElement( name = ValidationReportV0.LOCATIONINFO_ELEMENT )
	private LocationInfoV0 locationInfo;

	public ConstraintViolationV0()
	{
		locationInfo = null;
	}

	public ConstraintViolationV0( ConstraintViolation constraintViolation )
	{
		this();
		message = constraintViolation.getMessage();
		constraintViolation.getLocationInfo().ifPresent( li -> locationInfo = new LocationInfoV0( li ) );
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage( String message )
	{
		this.message = message;
	}

	public LocationInfoV0 getLocationInfo()
	{
		return locationInfo;
	}

	public void setLocationInfo( LocationInfoV0 locationInfo )
	{
		this.locationInfo = locationInfo;
	}

	@Override
	public boolean equals( Object o )
	{
		if ( this == o ) return true;
		if ( o == null || getClass() != o.getClass() ) return false;
		ConstraintViolationV0 that = (ConstraintViolationV0) o;
		return Objects.equals( message, that.message ) &&
			Objects.equals( locationInfo, that.locationInfo );
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( message, locationInfo );
	}
}
