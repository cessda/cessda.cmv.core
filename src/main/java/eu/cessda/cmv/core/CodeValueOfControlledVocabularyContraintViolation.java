package eu.cessda.cmv.core;

class CodeValueOfControlledVocabularyContraintViolation extends MandatoryNodeConstraintViolation
{
	private String codeValue;
	private String cvUri;

	public CodeValueOfControlledVocabularyContraintViolation( String nodePath, String codeValue, String cvUri )
	{
		super( nodePath );
		this.codeValue = codeValue;
		this.cvUri = cvUri;
	}

	@Override
	public String getMessage()
	{
		String message = "CodeValue '%s' in '%s' is not element of the controlled vocabulary in '%s'";
		return String.format( message, codeValue, super.xPath, cvUri );
	}
}
