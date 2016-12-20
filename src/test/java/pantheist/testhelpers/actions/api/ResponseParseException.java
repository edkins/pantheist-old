package pantheist.testhelpers.actions.api;

public class ResponseParseException extends RuntimeException
{
	private static final long serialVersionUID = 1294635143901555563L;

	public ResponseParseException(final String message, final Exception cause)
	{
		super(message, cause);
	}
}
