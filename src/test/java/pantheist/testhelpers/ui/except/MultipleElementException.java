package pantheist.testhelpers.ui.except;

/**
 * Thrown when a css path matches multiple elements, and it's not supposed to.
 */
public class MultipleElementException extends RuntimeException
{
	private static final long serialVersionUID = -8846903164060569694L;

	public MultipleElementException(final String message)
	{
		super(message);
	}
}
