package pantheist.common.except;

public class AlreadyPresentException extends Exception
{
	private static final long serialVersionUID = 5548810351279446963L;

	public AlreadyPresentException(final String name)
	{
		super(name + " already exists");
	}
}
