package eu.cessda.cmv.core;

import java.util.List;

public interface Document
{
	public interface V10 extends Document
	{
		public List<Node> getNodes( String locationPath );
	}
}