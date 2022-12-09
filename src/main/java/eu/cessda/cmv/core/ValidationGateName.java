/*-
 * #%L
 * cmv-core
 * %%
 * Copyright (C) 2020 - 2021 CESSDA ERIC
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

@SuppressWarnings( "deprecation" )
public enum ValidationGateName
{
	BASIC( new BasicValidationGate() ),
	BASICPLUS( new BasicPlusValidationGate() ),
	STANDARD( new StandardValidationGate() ),
	EXTENDED( new ExtendedValidationGate() ),
	STRICT( new StrictValidationGate() );

	private final ValidationGate.V10 validationGate;

	ValidationGateName( ValidationGate.V10 validationGate )
	{
		this.validationGate = validationGate;
	}

	public ValidationGate.V10 getValidationGate()
	{
		return validationGate;
	}
}
