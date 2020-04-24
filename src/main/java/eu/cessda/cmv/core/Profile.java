package eu.cessda.cmv.core;

import java.util.List;

public interface Profile
{
	public interface V10 extends Profile
	{
		public <T extends Constraint> List<T> getConstraints();
	}
}
