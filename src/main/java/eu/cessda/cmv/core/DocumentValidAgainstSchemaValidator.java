package eu.cessda.cmv.core;

import java.util.Optional;

import org.gesis.commons.xml.XmlNotValidAgainstSchemaException;
import org.gesis.commons.xml.XmlSchemaNotFoundException;

class DocumentValidAgainstSchemaValidator implements Validator.V10
{
	private Document.V12 document;

	public DocumentValidAgainstSchemaValidator( Document document )
	{
		this.document = (Document.V12) document;
	}

	@Override
	public Optional<ConstraintViolation> validate()
	{
		try
		{
			document.validate();
			return Optional.empty();
		}
		catch (XmlNotValidAgainstSchemaException e)
		{
			String message = "Document is not valid against schema with %s violations";
			return Optional.of( new ConstraintViolation( String.format( message, e.getValidationResults().size() ),
					Optional.empty() ) );
		}
		catch (XmlSchemaNotFoundException e)
		{
			String message = "XML Schema location is missing";
			return Optional.of( new ConstraintViolation( message, Optional.empty() ) );
		}
	}
}
