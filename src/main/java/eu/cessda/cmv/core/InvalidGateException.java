package eu.cessda.cmv.core;

import java.util.List;

/**
 * Represents errors that occur when constructing a validation gate.
 */
public class InvalidGateException extends Exception
{
	private static final long serialVersionUID = -4231671316452274178L;

	/**
	 * Construct a new instance of {@link InvalidGateException} with the given causes. These causes will be treated
	 * as suppressed exceptions.
	 *
	 * @param causes the causes.
	 */
	InvalidGateException( List<InvalidConstraintException> causes )
	{
		super( String.format( "Gate construction failed: %s", causes.toString() ) );
		causes.forEach( this::addSuppressed );
	}
}
