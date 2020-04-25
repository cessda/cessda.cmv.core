package eu.cessda.cmv.core;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import org.gesis.commons.test.architecture.TestClassesRuleTest;
import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

public class ArchitectureConstraintsTest extends TestClassesRuleTest
{
	private JavaClasses importedClasses;
	private String[] packageNames;

	public ArchitectureConstraintsTest()
	{
		packageNames = new String[] { this.getClass().getPackage().getName() };
		importedClasses = new ClassFileImporter().importPackages( packageNames );
	}

	@Test
	public void testClassesShouldEndWithTestSuffix()
	{
		testClassesShouldEndWithTestSuffix( packageNames );
	}

	@Test
	public void ensureEncapsulation()
	{
		classes().that()
				.areNotInterfaces()
				.and().doNotImplement( Document.V10.class )
				.and().doNotImplement( Profile.V10.class )
				.and().areNotAssignableFrom( ConstraintViolation.class )
				.and().areNotAssignableTo( AbstractValidationGate.class )
				.and().haveSimpleNameNotEndingWith( "Test" )
				.should().bePackagePrivate()
				.check( importedClasses );
	}
}
