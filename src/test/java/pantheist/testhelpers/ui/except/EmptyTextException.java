package pantheist.testhelpers.ui.except;

/**
 * Thrown when an element is supposedly visible but contains no actual text.
 */
public class EmptyTextException extends RuntimeException
{
	private static final long serialVersionUID = -8327264050769391742L;

	public EmptyTextException(final String message)
	{
		super(message);
	}
}
