package eu.cessda.cmv.core.mediatype.profile.v0;

import javax.xml.bind.annotation.XmlElement;

public abstract class NodeConstraint extends ConstraintV0
{
	@XmlElement
	protected String locationPath;

	protected NodeConstraint( String locationPath )
	{
		this.locationPath = locationPath;
	}

	public String getLocationPath()
	{
		return locationPath;
	}

	public void setLocationPath( String locationPath )
	{
		this.locationPath = locationPath;
	}
}