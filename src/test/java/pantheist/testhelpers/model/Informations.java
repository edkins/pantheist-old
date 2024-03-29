package pantheist.testhelpers.model;

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Informations
{
	private Informations()
	{
		throw new UnsupportedOperationException();
	}

	public static Information empty()
	{
		return new EmptyInformation();
	}

	public static Information string(final String value)
	{
		return ObjectInformation.ofNullable(value);
	}

	public static Information optionalString(final Optional<String> value)
	{
		if (value.isPresent())
		{
			return string(value.get());
		}
		else
		{
			return empty();
		}
	}

	public static Information jsonOrEmpty(final ObjectMapper objectMapper, final String json)
	{
		if (json == null || json.isEmpty())
		{
			return EmptyInformation.empty();
		}
		try
		{
			return ObjectInformation.ofNullable(objectMapper.readValue(json, Object.class));
		}
		catch (final IOException e)
		{
			throw new IllegalArgumentException(e);
		}
	}
}
