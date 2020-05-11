package eu.cessda.cmv.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbCompilableXPathConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbMandatoryNodeConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbMaximumElementOccuranceConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbOptionalNodeConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbPredicatelessXPathConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbProfileV0;
import eu.cessda.cmv.core.mediatype.profile.v0.xml.JaxbRecommendedNodeConstraintV0;

class DomProfile implements Profile.V10
{
	private List<Constraint.V20> constraints;

	public DomProfile( InputStream inputStream )
	{
		constraints = new ArrayList<>();

		JaxbProfileV0 profile = JaxbProfileV0.read( inputStream );
		for ( JaxbConstraintV0 constraint : profile.getConstraints() )
		{
			if ( constraint instanceof JaxbCompilableXPathConstraintV0 )
			{
				parse( (JaxbCompilableXPathConstraintV0) constraint );
			}
			else if ( constraint instanceof JaxbPredicatelessXPathConstraintV0 )
			{
				parse( (JaxbPredicatelessXPathConstraintV0) constraint );
			}
			else if ( constraint instanceof JaxbMandatoryNodeConstraintV0 )
			{
				parse( (JaxbMandatoryNodeConstraintV0) constraint );
			}
			else if ( constraint instanceof JaxbRecommendedNodeConstraintV0 )
			{
				parse( (JaxbRecommendedNodeConstraintV0) constraint );
			}
			else if ( constraint instanceof JaxbOptionalNodeConstraintV0 )
			{
				parse( (JaxbOptionalNodeConstraintV0) constraint );
			}
			else if ( constraint instanceof JaxbMaximumElementOccuranceConstraintV0 )
			{
				parse( (JaxbMaximumElementOccuranceConstraintV0) constraint );
			}
		}
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Constraint> List<T> getConstraints()
	{
		return (List<T>) constraints;
	}

	private void parse( JaxbMaximumElementOccuranceConstraintV0 jaxbConstraint )
	{
		MaximumElementOccuranceConstraint constraint = new MaximumElementOccuranceConstraint(
				jaxbConstraint.getLocationPath(),
				jaxbConstraint.getMaxOccurs() );
		constraints.add( constraint );
	}

	private void parse( JaxbOptionalNodeConstraintV0 jaxbConstraint )
	{
		OptionalNodeConstraint constraint = new OptionalNodeConstraint( jaxbConstraint.getLocationPath() );
		constraints.add( constraint );
	}

	private void parse( JaxbRecommendedNodeConstraintV0 jaxbConstraint )
	{
		RecommendedNodeConstraint constraint = new RecommendedNodeConstraint( jaxbConstraint.getLocationPath() );
		constraints.add( constraint );
	}

	private void parse( JaxbMandatoryNodeConstraintV0 jaxbConstraint )
	{
		MandatoryNodeConstraint constraint = new MandatoryNodeConstraint( jaxbConstraint.getLocationPath() );
		constraints.add( constraint );
	}

	private void parse( JaxbPredicatelessXPathConstraintV0 jaxbConstraint )
	{
		PredicatelessXPathConstraint constraint = new PredicatelessXPathConstraint( jaxbConstraint.getLocationPath() );
		constraints.add( constraint );
	}

	private void parse( JaxbCompilableXPathConstraintV0 jaxbConstraint )
	{
		CompilableXPathConstraint constraint = new CompilableXPathConstraint( jaxbConstraint.getLocationPath() );
		constraints.add( constraint );
	}
}
