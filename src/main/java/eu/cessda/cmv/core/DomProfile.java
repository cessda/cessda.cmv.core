package eu.cessda.cmv.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import eu.cessda.cmv.core.mediatype.profile.v0.CompilableXPathConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.ConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.MandatoryNodeConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.MaximumElementOccuranceConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.OptionalNodeConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.PredicatelessXPathConstraintV0;
import eu.cessda.cmv.core.mediatype.profile.v0.ProfileV0;
import eu.cessda.cmv.core.mediatype.profile.v0.RecommendedNodeConstraintV0;

class DomProfile implements Profile.V10
{
	private List<Constraint.V20> constraints;

	public DomProfile( InputStream inputStream )
	{
		constraints = new ArrayList<>();

		ProfileV0 profile = ProfileV0.read( inputStream );
		for ( ConstraintV0 constraint : profile.getConstraints() )
		{
			if ( constraint instanceof CompilableXPathConstraintV0 )
			{
				parse( (CompilableXPathConstraintV0) constraint );
			}
			else if ( constraint instanceof PredicatelessXPathConstraintV0 )
			{
				parse( (PredicatelessXPathConstraintV0) constraint );
			}
			else if ( constraint instanceof MandatoryNodeConstraintV0 )
			{
				parse( (MandatoryNodeConstraintV0) constraint );
			}
			else if ( constraint instanceof RecommendedNodeConstraintV0 )
			{
				parse( (RecommendedNodeConstraintV0) constraint );
			}
			else if ( constraint instanceof OptionalNodeConstraintV0 )
			{
				parse( (OptionalNodeConstraintV0) constraint );
			}
			else if ( constraint instanceof MaximumElementOccuranceConstraintV0 )
			{
				parse( (MaximumElementOccuranceConstraintV0) constraint );
			}
		}
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <T extends Constraint> List<T> getConstraints()
	{
		return (List<T>) constraints;
	}

	private void parse( MaximumElementOccuranceConstraintV0 jaxbConstraint )
	{
		MaximumElementOccuranceConstraint constraint = new MaximumElementOccuranceConstraint(
				jaxbConstraint.getLocationPath(),
				jaxbConstraint.getMaxOccurs() );
		constraints.add( constraint );
	}

	private void parse( OptionalNodeConstraintV0 jaxbConstraint )
	{
		OptionalNodeConstraint constraint = new OptionalNodeConstraint( jaxbConstraint.getLocationPath() );
		constraints.add( constraint );
	}

	private void parse( RecommendedNodeConstraintV0 jaxbConstraint )
	{
		RecommendedNodeConstraint constraint = new RecommendedNodeConstraint( jaxbConstraint.getLocationPath() );
		constraints.add( constraint );
	}

	private void parse( MandatoryNodeConstraintV0 jaxbConstraint )
	{
		MandatoryNodeConstraint constraint = new MandatoryNodeConstraint( jaxbConstraint.getLocationPath() );
		constraints.add( constraint );
	}

	private void parse( PredicatelessXPathConstraintV0 jaxbConstraint )
	{
		PredicatelessXPathConstraint constraint = new PredicatelessXPathConstraint( jaxbConstraint.getLocationPath() );
		constraints.add( constraint );
	}

	private void parse( CompilableXPathConstraintV0 jaxbConstraint )
	{
		CompilableXPathConstraint constraint = new CompilableXPathConstraint( jaxbConstraint.getLocationPath() );
		constraints.add( constraint );
	}
}
