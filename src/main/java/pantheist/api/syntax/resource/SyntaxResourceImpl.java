package pantheist.api.syntax.resource;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pantheist.api.generic.backend.GenericBackend;
import pantheist.api.syntax.backend.SyntaxBackend;
import pantheist.api.syntax.model.TryOutTextReport;
import pantheist.common.except.AlreadyPresentException;
import pantheist.common.except.InvalidLocationException;
import pantheist.common.except.NotFoundException;
import pantheist.common.http.HttpHelper;
import pantheist.common.model.EmptyObject;

@Path("/syntax")
public final class SyntaxResourceImpl implements SyntaxResource
{
	private static final Logger LOGGER = LogManager.getLogger(SyntaxResourceImpl.class);
	private final GenericBackend backend;
	private final HttpHelper httpHelper;
	private final SyntaxBackend syntaxBackend;

	@Inject
	SyntaxResourceImpl(final GenericBackend backend, final HttpHelper httpHelper, final SyntaxBackend syntaxBackend)
	{
		this.backend = checkNotNull(backend);
		this.httpHelper = checkNotNull(httpHelper);
		this.syntaxBackend = checkNotNull(syntaxBackend);
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
			return httpHelper.jsonResponse(backend.listResources("syntax"));
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
			backend.createEmptyResource("syntax", syntaxId);
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
			backend.deleteResource("syntax", syntaxId);
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
	// component
	//
	///////////////

	@GET
	@Path("{syn}/{t}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response listComponents(@PathParam("syn") final String syntaxId, @PathParam("t") final String t)
	{
		try
		{
			return httpHelper.jsonResponse(backend.listComponents("syntax", syntaxId, t));
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
			@PathParam("n") final String componentId)
	{
		try
		{
			return httpHelper
					.jsonResponse(backend.getComponent("syntax", syntaxId, t, componentId));
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
			@HeaderParam("X-Allow-Replace") final boolean allowReplace,
			final String requestJson)
	{
		try
		{
			LOGGER.info("PUT /syntax/{}/{}/{} {} {}", syntaxId, t, componentId, allowReplace, requestJson);
			final Object data = httpHelper.parseRequest(
					requestJson,
					backend.desiredComponentType("syntax", syntaxId, t, componentId));
			backend.putComponent("syntax", syntaxId, t, componentId, data, allowReplace);
			return Response.noContent().build();
		}
		catch (final NotFoundException e)
		{
			throw httpHelper.rethrow(e);
		}
		catch (final InvalidLocationException e)
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
			LOGGER.info("DELETE /syntax/{}/{}/{}", syntaxId, t, componentId);
			backend.deleteComponent("syntax", syntaxId, t, componentId);
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

	@POST
	@Path("{syn}/try")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response tryOutText(@PathParam("syn") final String syntaxId, final String text)
	{
		try
		{
			LOGGER.info("POST /syntax/{}/try {}", syntaxId, text);
			final TryOutTextReport report = syntaxBackend.tryOutText(syntaxId, text);
			LOGGER.info("What happened: {}", report.whatHappened());
			return httpHelper.jsonResponse(report);
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
