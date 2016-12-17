package pantheist.api.syntax.resource;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pantheist.api.syntax.backend.SyntaxBackend;
import pantheist.common.http.HttpHelper;

@Path("/syntax")
public final class SyntaxResourceImpl implements SyntaxResource
{
	private static final Logger LOGGER = LogManager.getLogger(SyntaxResourceImpl.class);
	private final SyntaxBackend backend;
	private final HttpHelper httpHelper;

	@Inject
	SyntaxResourceImpl(final SyntaxBackend backend, final HttpHelper httpHelper)
	{
		this.backend = checkNotNull(backend);
		this.httpHelper = checkNotNull(httpHelper);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSyntax()
	{
		try
		{
			return httpHelper.jsonResponse(this.backend.listSyntax());
		}
		catch (final RuntimeException e)
		{
			LOGGER.catching(e);
			throw e;
		}
	}
}
