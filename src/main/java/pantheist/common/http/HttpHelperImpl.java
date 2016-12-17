package pantheist.common.http;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

final class HttpHelperImpl implements HttpHelper
{
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

}
