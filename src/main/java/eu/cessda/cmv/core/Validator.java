package eu.cessda.cmv.core;

import java.util.Optional;

public interface Validator
{
	public interface V10 extends Validator
	{
		Optional<ConstraintViolation> validate();
	}
}
