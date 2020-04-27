package eu.cessda.cmv.core.mediatype.validationreport.v0.xml;

import static eu.cessda.cmv.core.mediatype.validationreport.v0.xml.JaxbValidationReportV0.LOCATIONINFO_TYPE;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.gesis.commons.xml.LocationInfo;

@XmlType( name = LOCATIONINFO_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class JaxbLocationInfoV0
{
	@XmlElement
	private int lineNumber;

	@XmlElement
	private int columnNumber;

	public JaxbLocationInfoV0()
	{
	}

	public JaxbLocationInfoV0( LocationInfo locationInfo )
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
