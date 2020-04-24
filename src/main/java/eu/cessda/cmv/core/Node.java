package eu.cessda.cmv.core;

class Node
{
	private String locationPath;
	private String textContent;
	private int lineNumber;
	private int columnNumber;

	Node( String locationPath, String textContent )
	{
		this( locationPath, textContent, 0, 0 );
	}

	Node( String locationPath, String textContent, int lineNumber, int columnNumber )
	{
		this.locationPath = locationPath;
		this.textContent = textContent;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
	}

	public String getLocationPath()
	{
		return locationPath;
	}

	public String getTextContent()
	{
		return textContent;
	}

	public int getLineNumber()
	{
		return lineNumber;
	}

	public int getColumnNumber()
	{
		return columnNumber;
	}
}
