package pantheist.common.except;

/**
 * Indicates that the user is trying to create a resource in a location that
 * doesn't make sense.
 */
public class InvalidLocationException extends Exception
{
	private static final long serialVersionUID = -1357036049927515131L;

	public InvalidLocationException(final String path)
	{
		super("Invalid location: " + path);
	}
}
