package pantheist.common.http;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pantheist.common.except.AlreadyPresentException;
import pantheist.common.except.NotFoundException;

final class HttpHelperImpl implements HttpHelper
{
	private static final Logger LOGGER = LogManager.getLogger(HttpHelperImpl.class);
	private final ObjectMapper objectMapper;

	@Inject
	HttpHelperImpl(final ObjectMapper objectMapper)
	{
		this.objectMapper = checkNotNull(objectMapper);
	}

	@Override
	public Response jsonResponse(final Object payload)
	{
		try
		{
			final String json = objectMapper.writeValueAsString(payload);
			return Response.ok(json).build();
		}
		catch (final JsonProcessingException e)
		{
			throw new HttpHelperException(e);
		}
	}

	@Override
	public <T> T parseRequest(final String json, final Class<T> clazz)
	{
		try
		{
			return objectMapper.readValue(json, clazz);
		}
		catch (final IOException e)
		{
			throw new WebApplicationException(e, 400);
		}
	}

	@Override
	public <T> T parseRequest(final String json, final TypeReference<? extends T> typeRef)
	{
		try
		{
			return objectMapper.readValue(json, typeRef);
		}
		catch (final IOException e)
		{
			throw new WebApplicationException(e, 400);
		}
	}

	@Override
	public RuntimeException rethrow(final AlreadyPresentException ex)
	{
		LOGGER.catching(ex);
		throw new WebApplicationException(ex, 400);
	}

	@Override
	public RuntimeException rethrow(final NotFoundException ex)
	{
		LOGGER.catching(ex);
		throw new WebApplicationException(ex, 404);
	}

}
