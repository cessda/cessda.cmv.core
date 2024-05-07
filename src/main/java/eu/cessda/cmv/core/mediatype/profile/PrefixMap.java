package eu.cessda.cmv.core.mediatype.profile;

import javax.xml.bind.annotation.XmlElement;

public class PrefixMap
{
	@XmlElement
	private String prefix;

	@XmlElement
	private String namespace;

	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix( String prefix )
	{
		this.prefix = prefix;
	}

	public String getNamespace()
	{
		return namespace;
	}

	public void setNamespace( String namespace )
	{
		this.namespace = namespace;
	}
}
