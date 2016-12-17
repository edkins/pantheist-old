package pantheist.api.syntax.resource;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pantheist.api.syntax.backend.SyntaxBackend;
import pantheist.common.except.AlreadyPresentException;
import pantheist.common.except.NotFoundException;
import pantheist.common.http.HttpHelper;
import pantheist.common.model.EmptyObject;

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
	public Response listSyntax()
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

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putSyntax(@PathParam("id") final String id, final String requestJson)
	{
		try
		{
			LOGGER.info("PUT /syntax/{} {}", id, requestJson);
			httpHelper.parseRequest(requestJson, EmptyObject.class);
			backend.createSyntax(id);
			return Response.status(201).build();
		}
		catch (final AlreadyPresentException e)
		{
			throw httpHelper.rethrow(e);
		}
		catch (final RuntimeException e)
		{
			LOGGER.catching(e);
			throw e;
		}
	}

	@DELETE
	@Path("/{id}")
	public Response deleteSyntax(@PathParam("id") final String id)
	{
		LOGGER.info("DELETE /syntax/{}", id);
		try
		{
			backend.deleteSyntax(id);
			return Response.noContent().build();
		}
		catch (final NotFoundException e)
		{
			throw httpHelper.rethrow(e);
		}
		catch (final RuntimeException e)
		{
			LOGGER.catching(e);
			throw e;
		}
	}
}
