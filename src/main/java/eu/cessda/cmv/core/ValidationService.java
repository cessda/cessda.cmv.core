package eu.cessda.cmv.core;

import java.net.URL;

import org.gesis.commons.resource.Resource;

import eu.cessda.cmv.core.mediatype.validationreport.v0.ValidationReportV0;

public interface ValidationService
{
	public interface V10 extends ValidationService
	{
		public ValidationReportV0 validate(
				URL documentUrl,
				URL profileUrl,
				ValidationGateName validationGateName );

		public ValidationReportV0 validate(
				Resource document,
				Resource profile,
				ValidationGateName validationGateName );
	}
}