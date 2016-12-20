package pantheist.testhelpers.ui.except;

/**
 * Thrown when text is not what it should have been
 */
public class IncorrectTextException extends UiStateException
{
	private static final long serialVersionUID = -502369648565104529L;

	public IncorrectTextException(final String message)
	{
		super(message);
	}
}
