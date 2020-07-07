package eu.cessda.cmv.core;

import java.net.URL;

import org.gesis.commons.resource.Resource;

public interface ValidationService
{
	public interface V10 extends ValidationService
	{
		public <T extends ValidationReport> T validate(
				URL documentUrl,
				URL profileUrl,
				ValidationGateName validationGateName );

		public <T extends ValidationReport> T validate(
				Resource document,
				Resource profile,
				ValidationGateName validationGateName );
	}
}