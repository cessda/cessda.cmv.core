package eu.cessda.cmv.core;

class Node
{
	private String textContent;
	private int lineNumber;
	private int columnNumber;

	Node( String textContent )
	{
		this( textContent, 0, 0 );
	}

	Node( String textContent, int lineNumber, int columnNumber )
	{
		this.textContent = textContent;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
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
