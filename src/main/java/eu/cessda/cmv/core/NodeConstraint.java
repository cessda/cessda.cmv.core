package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

abstract class NodeConstraint implements Constraint.V20
{
	private String locationPath;

	NodeConstraint( String locationPath )
	{
		requireNonNull( locationPath );
		this.locationPath = locationPath;
	}

	protected String getLocationPath()
	{
		return locationPath;
	}
}
