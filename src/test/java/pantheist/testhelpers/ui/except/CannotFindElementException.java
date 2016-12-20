package pantheist.testhelpers.ui.except;

/**
 * Thrown when an action is requested on a web element which does not exist, or
 * is invisible.
 */
public class CannotFindElementException extends RuntimeException
{
	private static final long serialVersionUID = -3892874783096225446L;

	public CannotFindElementException(final String message)
	{
		super(message);
	}
}
