package eu.cessda.cmv.core;

import static java.util.Optional.empty;

import java.util.Optional;

class MandatoryNodeValidator implements Validator.V10
{
	private String locationPath;
	private long count;

	public MandatoryNodeValidator( String locationPath, long count )
	{
		this.locationPath = locationPath;
		this.count = count;
	}

	@Override
	public <T extends ConstraintViolation> Optional<T> validate()
	{
		if ( count == 0 )
		{
			return Optional.of( (T) new MandatoryNodeConstraintViolation( locationPath ) );
		}
		else
		{
			return empty();
		}
	}
}
