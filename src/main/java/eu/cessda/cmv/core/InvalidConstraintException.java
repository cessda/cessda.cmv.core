/*-
 * #%L
 * cmv-core
 * %%
 * Copyright (C) 2020 - 2023 CESSDA ERIC
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
