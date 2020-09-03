package eu.cessda.cmv.core;

import static java.util.Optional.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

class NodeTest
{
	@Test
	void getChildCount()
	{
		Node node = new Node( "/path/to/element", "value", empty() );
		String locationPath = "./@attribute";
		assertThat( node.getChildCount( locationPath ), equalTo( 0 ) );
		node.incrementChildCount( locationPath );
		node.incrementChildCount( locationPath );
		node.incrementChildCount( locationPath );
		assertThat( node.getChildCount( locationPath ), equalTo( 3 ) );
	}
}
