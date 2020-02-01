package eu.cessda.cmv.core;

public interface ConstraintViolation
{
	public interface V10 extends ConstraintViolation
	{
		/*
		 * @return the interpolated error message for this constraint violation
		 */
		public String getMessage();
	}
}
