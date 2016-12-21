package pantheist.testhelpers.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

public class JsonBuilder
{
	private final ObjectMapper objectMapper;

	// State
	private final Map<String, Object> obj;

	private JsonBuilder(final ObjectMapper objectMapper)
	{
		this.objectMapper = checkNotNull(objectMapper);
		this.obj = new HashMap<>();
	}

	public static JsonBuilder from(final ObjectMapper objectMapper)
	{
		return new JsonBuilder(objectMapper);
	}

	public JsonBuilder with(final String key, final String value)
	{
		checkNotNullOrEmpty(key);
		if (obj.containsKey(key))
		{
			throw new IllegalStateException("Already contains key " + key);
		}
		obj.put(key, value);
		return this;
	}

	public JsonBuilder with(final String key, final List<String> value)
	{
		checkNotNullOrEmpty(key);
		if (obj.containsKey(key))
		{
			throw new IllegalStateException("Already contains key " + key);
		}
		obj.put(key, value);
		return this;
	}

	@Override
	public String toString()
	{
		try
		{
			return objectMapper.writeValueAsString(obj);
		}
		catch (final JsonProcessingException e)
		{
			throw Throwables.propagate(e);
		}
	}

	public Entity<String> toEntity()
	{
		return Entity.json(this.toString());
	}
}
