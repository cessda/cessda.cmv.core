package eu.cessda.cmv.core;

import static org.junit.Assert.assertTrue;

import org.gesis.commons.test.DefaultTestEnv;
import org.gesis.commons.test.TestEnv;
import org.gesis.commons.xml.SaxXercesAgainstSchemaValidator;
import org.gesis.commons.xml.ddi.Ddi251ClasspathEntityResolver;
import org.junit.jupiter.api.Test;

public class MandatoryNodeIfParentPresentConstraintTest
{
	private TestEnv.V13 testEnv;
	private CessdaMetadataValidatorFactory factory;

	public MandatoryNodeIfParentPresentConstraintTest()
	{
		testEnv = DefaultTestEnv.newInstance( MandatoryNodeIfParentPresentConstraintTest.class );
		factory = new CessdaMetadataValidatorFactory();
		SaxXercesAgainstSchemaValidator xmlValidator = new SaxXercesAgainstSchemaValidator();
		xmlValidator.validate( testEnv.findTestResourceByName( "cdc25_GESIS_ZA2800_ex22invalid.xml" ), new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "cdc25_GESIS_ZA2800_ex22invalid2.xml" ), new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "cdc25_GESIS_ZA2800_ex22valid.xml" ), new Ddi251ClasspathEntityResolver() );
		xmlValidator.validate( testEnv.findTestResourceByName( "cdc25_profile_ex22.xml" ), new Ddi251ClasspathEntityResolver() );
	}

	@Test
	public void test()
	{
		assertTrue( true );
	}
}
