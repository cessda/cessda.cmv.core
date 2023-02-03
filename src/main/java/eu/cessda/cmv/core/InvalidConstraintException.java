package eu.cessda.cmv.core;

/**
 * Represents errors that occur when attempting to look up a constraint.
 */
public class InvalidConstraintException extends Exception
{
	private static final long serialVersionUID = 4324993248911039451L;
	private final String constraintName;

	/**
	 * Construct a new instance of {@link InvalidConstraintException} with the specified constraint name and cause.
	 *
	 * @param constraintName the name of the constraint that was looked up.
	 * @param cause          the cause of the lookup failure.
	 */
	InvalidConstraintException( String constraintName, Throwable cause )
	{
		super( String.format( "%s is not a valid constraint", constraintName ), cause );
		this.constraintName = constraintName;
	}

	/**
	 * Get the name of the constraint that was looked up.
	 */
	public String getConstraintName()
	{
		return constraintName;
	}
}
