package eu.cessda.cmv.core;

import static org.junit.Assert.assertTrue;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.gesis.commons.xml.SaxXercesAgainstSchemaValidator;
import org.gesis.commons.xml.ddi.Ddi251ClasspathEntityResolver;
import org.junit.jupiter.api.Test;

public class MandatoryNodeIfParentPresentConstraintTest
{
	private TestEnv.V14 testEnv;

	public MandatoryNodeIfParentPresentConstraintTest()
	{
		testEnv = DefaultTestEnv.newInstance( MandatoryNodeIfParentPresentConstraintTest.class );
		SaxXercesAgainstSchemaValidator xmlValidator = new SaxXercesAgainstSchemaValidator();
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/22-document-invalid-1.xml" ),
				new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/22-document-invalid-2.xml" ),
				new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/22-document-valid.xml" ),
				new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "ddi-v25/22-profile.xml" ) );
	}

	@Test
	public void test()
	{
		assertTrue( true );
	}
}
