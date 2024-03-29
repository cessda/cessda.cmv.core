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

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.gesis.commons.resource.ResourceLabelProvider;
import org.gesis.commons.test.architecture.TestClassesRuleTest;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

class ArchitectureConstraintsTest extends TestClassesRuleTest
{
	private static final JavaClasses importedClasses = new ClassFileImporter().importPackages( ArchitectureConstraintsTest.class.getPackage().getName() );

	@Test
	@SuppressWarnings( "java:S5786" )
	public void testClassesShouldEndWithTestSuffix()
	{
		methods().that()
				.areAnnotatedWith( org.junit.Test.class ).or()
				.areAnnotatedWith( org.junit.jupiter.api.Test.class ).or()
				.areAnnotatedWith( org.junit.jupiter.params.ParameterizedTest.class )
				.should()
				.beDeclaredInClassesThat()
				.haveSimpleNameEndingWith( "Test" )
				.check( importedClasses );
	}

	@Test
	void ensureEncapsulation()
	{
		// Everything else in 'eu.cessda.cmv.core' should be package private
		classes().that()
				.areNotInterfaces()
				.and().areNotEnums()
				.and().areNotAssignableFrom( ConstraintViolation.class )
				.and().areAssignableFrom( ResourceLabelProvider.class )
				.and().haveSimpleNameNotContaining( "CessdaMetadataValidatorFactory" )
				.and().haveSimpleNameNotEndingWith( "Test" )
				.and().haveSimpleNameNotEndingWith( "TestParameter" )
				.and().resideOutsideOfPackage( "eu.cessda.cmv.core.mediatype.(**)" )
				.and().resideOutsideOfPackage( "eu.cessda.cmv.core.controlledvocabulary" )
				.should().bePackagePrivate()
				.check( importedClasses );
	}
}
