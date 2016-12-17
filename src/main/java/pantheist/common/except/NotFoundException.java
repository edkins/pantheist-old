package pantheist.common.except;

public class NotFoundException extends Exception
{
	private static final long serialVersionUID = -5825530710160055511L;

	public NotFoundException(final String name)
	{
		super(name + " not found");
	}
}
