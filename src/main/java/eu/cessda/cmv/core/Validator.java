package eu.cessda.cmv.core;

import java.util.Optional;

interface Validator
{
	interface V10 extends Validator
	{
		<T extends ConstraintViolation> Optional<T> validate();
	}
}
