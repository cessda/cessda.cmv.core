package eu.cessda.cmv.core;

import java.util.List;

public interface ValidationGate
{
	public interface V10 extends ValidationGate
	{
		public <T extends ConstraintViolation> List<T> validate( Document document, Profile profile );
	}
}
