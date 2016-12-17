package pantheist.common.except;

public class ExceptionWritingException extends RuntimeException
{
	private static final long serialVersionUID = -6622876400703876022L;

	public ExceptionWritingException(final Exception ex)
	{
		super(ex);
	}
}
