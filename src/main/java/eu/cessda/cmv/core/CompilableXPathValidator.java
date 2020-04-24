package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

class CompilableXPathValidator implements Validator.V10
{
	private String locationPath;
	private XPathFactory factory;

	public CompilableXPathValidator( String locationPath )
	{
		this( locationPath, XPathFactory.newInstance() );
	}

	public CompilableXPathValidator( String locationPath, XPathFactory factory )
	{
		requireNonNull( locationPath );
		requireNonNull( factory );

		this.locationPath = locationPath;
		this.factory = factory;
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends ConstraintViolation> Optional<T> validate()
	{
		try
		{
			factory.newXPath().compile( locationPath );
			return Optional.empty();
		}
		catch (XPathExpressionException e)
		{
			String reason = e.getMessage().replace( "javax.xml.transform.TransformerException: ", "" );
			return Optional.of( (T) new CompilableXPathConstraintViolation( locationPath, reason ) );
		}
	}
}
