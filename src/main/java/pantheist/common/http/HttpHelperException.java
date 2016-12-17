package pantheist.common.http;

public class HttpHelperException extends RuntimeException
{
	private static final long serialVersionUID = -9038873455614399638L;

	public HttpHelperException(final Exception ex)
	{
		super(ex);
	}
}
