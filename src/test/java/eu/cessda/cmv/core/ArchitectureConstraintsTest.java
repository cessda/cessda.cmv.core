package eu.cessda.cmv.core;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import org.gesis.commons.test.architecture.TestClassesRuleTest;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

class ArchitectureConstraintsTest extends TestClassesRuleTest
{
	private JavaClasses importedClasses;
	private String[] packageNames;

	ArchitectureConstraintsTest()
	{
		packageNames = new String[] { this.getClass().getPackage().getName() };
		importedClasses = new ClassFileImporter().importPackages( packageNames );
	}

	@Test
	@SuppressWarnings( "java:S5786" )
	public void testClassesShouldEndWithTestSuffix()
	{
		testClassesShouldEndWithTestSuffix( packageNames );
	}

	@Test
	void ensureEncapsulation()
	{
		// Everything else in 'eu.cessda.cmv.core' should be package private
		classes().that()
				.areNotInterfaces()
				.and().areNotEnums()
				.and().areNotAssignableFrom( ConstraintViolation.class )
				.and().haveSimpleNameNotContaining( "CessdaMetadataValidatorFactory" )
				.and().haveSimpleNameNotEndingWith( "Test" )
				.and().haveSimpleNameNotEndingWith( "TestParameter" )
				.and().resideOutsideOfPackage( "eu.cessda.cmv.core.mediatype.(**)" )
				.and().resideOutsideOfPackage( "eu.cessda.cmv.core.controlledvocabulary" )
				.should().bePackagePrivate()
				.check( importedClasses );
	}
}
