package eu.cessda.cmv.core;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.gesis.commons.xml.LocationInfo;

class Node
{
	private String locationPath;
	private String textContent;
	private Optional<LocationInfo> locationInfo;
	private Map<String, Integer> childCounter;

	Node( String locationPath, String textContent, Optional<LocationInfo> locationInfo )
	{
		requireNonNull( locationPath );
		requireNonNull( locationInfo );

		this.locationPath = locationPath;
		this.textContent = textContent;
		this.locationInfo = locationInfo;
		childCounter = new HashMap<>();
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

	public void incrementChildCount( String relativeLocationPath )
	{
		requireNonNull( relativeLocationPath );
		if ( !childCounter.containsKey( relativeLocationPath ) )
		{
			childCounter.put( relativeLocationPath, 0 );
		}
		childCounter.put( relativeLocationPath, childCounter.get( relativeLocationPath ) + 1 );
	}

	public int getChildCount( String relativeLocationPath )
	{
		if ( childCounter.containsKey( relativeLocationPath ) )
		{
			return childCounter.get( relativeLocationPath );
		}
		else
		{
			return 0;
		}
	}
}
