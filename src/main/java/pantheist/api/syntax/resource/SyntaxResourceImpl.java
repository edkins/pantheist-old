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

import pantheist.api.syntax.backend.ComponentType;
import pantheist.api.syntax.backend.SyntaxBackend;
import pantheist.api.syntax.model.PutComponentRequest;
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

	///////////////
	//
	// syntax
	//
	///////////////

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
	@Path("/{syn}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putSyntax(@PathParam("syn") final String syntaxId, final String requestJson)
	{
		try
		{
			LOGGER.info("PUT /syntax/{} {}", syntaxId, requestJson);
			httpHelper.parseRequest(requestJson, EmptyObject.class);
			backend.createSyntax(syntaxId);
			return Response.noContent().build();
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
	@Path("/{syn}")
	public Response deleteSyntax(@PathParam("syn") final String syntaxId)
	{
		LOGGER.info("DELETE /syntax/{}", syntaxId);
		try
		{
			backend.deleteSyntax(syntaxId);
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

	///////////////
	//
	// node
	//
	///////////////

	private ComponentType ct(final String t) throws NotFoundException
	{
		try
		{
			return ComponentType.valueOf(t);
		}
		catch (final IllegalArgumentException e)
		{
			throw new NotFoundException(e);
		}
	}

	@GET
	@Path("{syn}/{t}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listComponents(@PathParam("syn") final String syntaxId, @PathParam("t") final String t)
	{
		try
		{
			return httpHelper.jsonResponse(this.backend.listComponents(syntaxId, ct(t)));
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

	@GET
	@Path("{syn}/{t}/{n}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getComponent(@PathParam("syn") final String syntaxId,
			@PathParam("t") final String t,
			@PathParam("n") final String nodeId)
	{
		try
		{
			return httpHelper.jsonResponse(this.backend.getComponent(syntaxId, ct(t), nodeId));
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

	@PUT
	@Path("{syn}/{t}/{n}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response putNode(@PathParam("syn") final String syntaxId,
			@PathParam("t") final String t,
			@PathParam("n") final String componentId,
			final String requestJson)
	{
		try
		{
			final ComponentType componentType = ct(t);
			final PutComponentRequest<?> request = httpHelper.parseRequest(
					requestJson,
					componentType.putRequestTypeRef());
			this.backend.putComponent(syntaxId, componentType, componentId, request);
			return Response.noContent().build();
		}
		catch (final NotFoundException e)
		{
			throw httpHelper.rethrow(e);
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
	@Path("{syn}/{t}/{n}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteNode(@PathParam("syn") final String syntaxId,
			@PathParam("t") final String t,
			@PathParam("n") final String componentId)
	{
		try
		{
			this.backend.deleteComponent(syntaxId, ct(t), componentId);
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
