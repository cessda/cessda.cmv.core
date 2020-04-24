package eu.cessda.cmv.core;

import java.util.List;

public interface ValidationReport
{
	public interface V10 extends ValidationReport
	{
		public <T extends ConstraintViolation> List<T> getConstraintViolations();
	}
}
