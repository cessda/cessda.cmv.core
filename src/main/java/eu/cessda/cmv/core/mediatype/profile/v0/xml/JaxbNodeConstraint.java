package eu.cessda.cmv.core.mediatype.profile.v0.xml;

import javax.xml.bind.annotation.XmlElement;

public abstract class JaxbNodeConstraint extends JaxbConstraintV0
{
	@XmlElement
	protected String locationPath;

	protected JaxbNodeConstraint( String locationPath )
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