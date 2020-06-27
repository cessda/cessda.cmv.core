package eu.cessda.cmv.core;

import static java.util.stream.Collectors.toList;
import static org.gesis.commons.xml.xpath.XPathTokenizer.PARENT;

import java.util.ArrayList;
import java.util.List;

import org.gesis.commons.xml.xpath.XPathTokenizer;

class MandatoryNodeIfParentPresentConstraint extends NodeConstraint
{
	public MandatoryNodeIfParentPresentConstraint( String locationPath )
	{
		super( locationPath );
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Validator> List<T> newValidators( Document document )
	{
		List<Validator> validators = new ArrayList<>();
		List<Node> nodes = ((Document.V11) document).getNodes( getLocationPath() );
		validators.addAll( nodes.stream().map( NotBlankNodeValidator::new ).collect( toList() ) );
		XPathTokenizer tokenizer = new XPathTokenizer( getLocationPath() );
		nodes = ((Document.V11) document).getNodes( tokenizer.getLocationPath( PARENT ) );
		for ( Node node : nodes )
		{
			validators.add( new MandatoryNodeIfParentPresentValidator( tokenizer, node ) );
		}
		return (List<T>) validators;
	}
}