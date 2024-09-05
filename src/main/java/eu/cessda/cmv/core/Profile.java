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

import javax.xml.namespace.NamespaceContext;
import java.util.Set;

/**
 * A profile is a set of constraints that can be used to validate XML documents. The CESSDA Metadata Validator
 * validates a document using these constraints using a validation gate, which defines which constraints are
 * used for validation.
 * <p>
 * Profiles are considered to be equal if each profile has the same constraints. Performing a validation using
 * equivalent profiles should emit identical constraint violations. The ordering of the constraints is unimportant.
 * <p>
 * The profile name and version is not considered important for the purposes of profile equality, and is ignored.
 */
public interface Profile
{
	/**
	 * Returns the name of the profile, or {@code null} if the profile doesn't have a name.
	 */
	String getProfileName();

	/**
	 * Returns the version of the profile, or {@code null} if the profile doesn't have a name.
	 */
	String getProfileVersion();

	/**
	 * A set of constraints that can be used to validate documents.
	 * @return a set of constraints.
	 */
	Set<Constraint> getConstraints();

	NamespaceContext getNamespaceContext();
}
