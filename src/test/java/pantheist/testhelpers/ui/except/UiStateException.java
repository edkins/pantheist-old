package pantheist.testhelpers.ui.except;

/**
 * The base class for all things having to do with the UI being in an unexpected
 * state.
 *
 * These are the exceptions that can be retried after a disruptive UI event.
 */
public abstract class UiStateException extends RuntimeException
{
	private static final long serialVersionUID = 1379114937701438220L;

	public UiStateException(final String message)
	{
		super(message);
	}
}
