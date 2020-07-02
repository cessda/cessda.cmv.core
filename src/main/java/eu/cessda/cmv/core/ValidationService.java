package eu.cessda.cmv.core;

import java.net.URL;

import eu.cessda.cmv.core.ValidationGateName;
import eu.cessda.cmv.core.mediatype.validationreport.v0.ValidationReportV0;

public interface ValidationService
{
	public interface V10 extends ValidationService
	{
		public ValidationReportV0 validate(
				URL documentUrl,
				URL profileUrl,
				ValidationGateName validationGateName );
	}
}