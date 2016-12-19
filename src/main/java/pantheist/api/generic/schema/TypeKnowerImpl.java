package pantheist.api.generic.schema;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

import pantheist.api.generic.store.OpenResource;
import pantheist.api.syntax.model.Syntax;

final class TypeKnowerImpl implements TypeKnower
{
	private final ObjectMapper objectMapper;

	@Inject
	TypeKnowerImpl(final ObjectMapper objectMapper)
	{
		this.objectMapper = checkNotNull(objectMapper);
	}

	@Override
	public boolean resourceTypeExists(final String resourceType)
	{
		switch (resourceType) {
		case "syntax":
			return true;
		}
		return false;
	}

	@Override
	public Class<? extends OpenResource> resourceClass(final String resourceType)
	{
		switch (resourceType) {
		case "syntax":
			return Syntax.class;
		}
		throw new IllegalArgumentException("Unknown resource type: " + resourceType);
	}

	@Override
	public OpenResource createEmptyResource(final String resourceType)
	{
		try
		{
			return objectMapper.readValue("{}", resourceClass(resourceType));
		}
		catch (final IOException e)
		{
			throw Throwables.propagate(e);
		}
	}

}
