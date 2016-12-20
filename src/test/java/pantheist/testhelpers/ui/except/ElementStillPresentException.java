package pantheist.testhelpers.ui.except;

/**
 * Thrown when an element still exists and shouldn't.
 */
public class ElementStillPresentException extends UiStateException
{
	private static final long serialVersionUID = 2818305799939616380L;

	public ElementStillPresentException(final String message)
	{
		super(message);
	}
}
