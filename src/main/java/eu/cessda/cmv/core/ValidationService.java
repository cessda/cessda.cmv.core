package eu.cessda.cmv.core;

import java.net.URI;

import org.gesis.commons.resource.Resource;

public interface ValidationService
{
	public interface V10 extends ValidationService
	{
		public <T extends ValidationReport> T validate(
				URI documentUri,
				URI profileUri,
				ValidationGateName validationGateName );

		public <T extends ValidationReport> T validate(
				Resource document,
				Resource profile,
				ValidationGateName validationGateName );
	}
}