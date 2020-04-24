package eu.cessda.cmv.core;

public interface ValidationGate
{
	public interface V10 extends ValidationGate
	{
		public <T extends ValidationReport> T validate( Document document, Profile profile );
	}
}
