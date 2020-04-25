package eu.cessda.cmv.core;

public class ConstraintViolation
{
	private String message;
	private int lineNumber;
	private int columnNumber;

	public ConstraintViolation( String message, int lineNumber, int columnNumber )
	{
		this.message = message;
		this.lineNumber = lineNumber;
		this.columnNumber = columnNumber;
	}

	public String getMessage()
	{
		return message;
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
