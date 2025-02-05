package org.gesis.commons.xml;

/**
 * Thrown when an XPath does not resolve an XML element.
 */
public class NoSuchNodeException extends Exception
{
	private static final long serialVersionUID = 8830661548083344096L;

	NoSuchNodeException( String xpath )
	{
		super( "No node matching " + xpath + " found" );
	}
}
