package eu.cessda.cmv.core;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

class CompilableXPathValidator
{
	private XPathFactory factory;

	public CompilableXPathValidator()
	{
		factory = XPathFactory.newInstance();
	}

	public boolean isValid( String xPath )
	{
		if ( xPath == null || xPath.isEmpty() )
		{
			return true;
		}
		try
		{
			factory.newXPath().compile( xPath );
			return true;
		}
		catch (XPathExpressionException e)
		{
			return false;
		}
	}
}