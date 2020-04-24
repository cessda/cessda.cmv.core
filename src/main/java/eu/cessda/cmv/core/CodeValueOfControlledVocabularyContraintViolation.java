package eu.cessda.cmv.core;

class CodeValueOfControlledVocabularyContraintViolation implements ConstraintViolation.V10
{
	private String locationPath;
	private String codeValue;
	private String cvUri;

	public CodeValueOfControlledVocabularyContraintViolation( String locationPath, String codeValue, String cvUri )
	{
		this.locationPath = locationPath;
		this.codeValue = codeValue;
		this.cvUri = cvUri;
	}

	@Override
	public String getMessage()
	{
		String message = "CodeValue '%s' in '%s' is not element of the controlled vocabulary in '%s'";
		return String.format( message, codeValue, locationPath, cvUri );
	}
}
