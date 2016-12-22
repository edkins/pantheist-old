package pantheist.engine.parser;

/**
 * Indicates that the grammar itself is not constructed correctly.
 */
public class BadGrammarException extends RuntimeException
{
	private static final long serialVersionUID = -6382390540185897684L;

	public BadGrammarException(final String message)
	{
		super("Grammar problem: " + message);
	}
}
