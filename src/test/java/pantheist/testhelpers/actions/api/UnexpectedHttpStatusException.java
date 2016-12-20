package pantheist.testhelpers.actions.api;

import javax.ws.rs.core.Response;

public class UnexpectedHttpStatusException extends RuntimeException
{
	private static final long serialVersionUID = -8182992950483501235L;

	private UnexpectedHttpStatusException(final String message)
	{
		super(message);
	}

	public static UnexpectedHttpStatusException forResponse(final Response response)
	{
		return new UnexpectedHttpStatusException(message(response));
	}

	private static String message(final Response response)
	{
		String entity = "";
		if (response.hasEntity())
		{
			entity = response.readEntity(String.class);
		}
		return response.getStatus() + " " + entity;
	}
}
