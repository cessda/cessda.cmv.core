package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import org.gesis.commons.xml.LocationInfo;

class Node
{
	private String locationPath;
	private String textContent;
	private Optional<LocationInfo> locationInfo;

	Node( String locationPath, String textContent, Optional<LocationInfo> locationInfo )
	{
		requireNonNull( locationPath );
		requireNonNull( locationInfo );

		this.locationPath = locationPath;
		this.textContent = textContent;
		this.locationInfo = locationInfo;
	}

	public String getLocationPath()
	{
		return locationPath;
	}

	public String getTextContent()
	{
		return textContent;
	}

	public Optional<LocationInfo> getLocationInfo()
	{
		return locationInfo;
	}
}
