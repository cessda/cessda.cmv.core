package eu.cessda.cmv.core.mediatype.validationrequest.v0;

import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

@XmlType( name = ValidationRequestV0.DOCUMENT_TYPE )
@XmlAccessorType( XmlAccessType.FIELD )
public class DocumentV0
{
	@XmlElement
	private URI uri;

	@XmlCDATA
	@XmlElement
	private String content;

	public URI getUri()
	{
		return uri;
	}

	public void setUri( URI uri )
	{
		this.uri = uri;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent( String content )
	{
		this.content = content;
	}
}
