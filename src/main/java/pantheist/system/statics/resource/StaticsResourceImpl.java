package pantheist.system.statics.resource;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pantheist.system.statics.backend.StaticsBackend;

@Path("/")
public final class StaticsResourceImpl implements StaticsResource
{
	private static final Logger LOGGER = LogManager.getLogger(StaticsResourceImpl.class);
	private final StaticsBackend backend;

	@Inject
	StaticsResourceImpl(final StaticsBackend backend)
	{
		this.backend = checkNotNull(backend);
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response getRoot()
	{
		return serve("index", "html");
	}

	@GET
	@Path("/static/{path:.*}.html")
	@Produces(MediaType.TEXT_HTML)
	public Response getHtml(@PathParam("path") final String path)
	{
		return serve(path, "html");
	}

	@GET
	@Path("/static/{path:.*}.js")
	@Produces("text/javascript")
	public Response getJs(@PathParam("path") final String path)
	{
		return serve(path, "js");
	}

	@GET
	@Path("/static/{path:.*}.css")
	@Produces("text/css")
	public Response getCss(@PathParam("path") final String path)
	{
		return serve(path, "css");
	}

	private Response serve(final String path, final String suffix)
	{
		try
		{
			final Optional<InputStream> input = backend.serveStaticFile("/static/" + path + "." + suffix);
			if (input.isPresent())
			{
				return Response.ok(input.get()).build();
			}
			else
			{
				return Response.status(404).entity("Not found").build();
			}
		}
		catch (final RuntimeException e)
		{
			LOGGER.catching(e);
			throw e;
		}
	}
}
