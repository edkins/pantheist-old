package pantheist.testhelpers.ui.except;

/**
 * Thrown if an action is requested on an element which is disabled.
 */
public class DisabledElementException extends RuntimeException
{
	private static final long serialVersionUID = 9056306222797568719L;

	public DisabledElementException(final String message)
	{
		super(message);
	}
}
