package eu.cessda.cmv.core.mediatype.validationreport.v0;

import static eu.cessda.cmv.core.mediatype.validationreport.v0.ValidationReportV0.LOCATIONINFO_TYPE;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.gesis.commons.xml.LocationInfo;

@XmlType( name = LOCATIONINFO_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class LocationInfoV0
{
	@XmlElement
	private int lineNumber;

	@XmlElement
	private int columnNumber;

	public LocationInfoV0()
	{
	}

	public LocationInfoV0( LocationInfo locationInfo )
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
}
