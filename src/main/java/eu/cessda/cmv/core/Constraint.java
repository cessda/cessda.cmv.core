package eu.cessda.cmv.core;

import java.util.List;

public interface Constraint
{
	public interface V10 extends Constraint
	{
		public <T extends ConstraintViolation> List<T> validate();
	}

	public interface V20 extends Constraint
	{
		public <T extends Validator> List<T> newValidators( Document document );
	}
}
