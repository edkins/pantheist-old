package pantheist.api.generic.schema;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import javax.inject.Inject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

import pantheist.api.generic.store.OpenResource;
import pantheist.api.syntax.model.PutComponentRequest;
import pantheist.api.syntax.model.Syntax;
import pantheist.api.syntax.model.SyntaxNode;
import pantheist.api.syntax.model.SyntaxToken;

final class TypeKnowerImpl implements TypeKnower
{
	private final ObjectMapper objectMapper;

	@Inject
	TypeKnowerImpl(final ObjectMapper objectMapper)
	{
		this.objectMapper = checkNotNull(objectMapper);
	}

	@Override
	public TypeReference<? extends PutComponentRequest<?>> putRequestTypeRef(
			final String resourceType,
			final String componentType)
	{
		switch (resourceType) {
		case "syntax":
			switch (componentType) {
			case "node":
				return new TypeReference<PutComponentRequest<SyntaxNode>>() {
				};
			case "token":
				return new TypeReference<PutComponentRequest<SyntaxToken>>() {
				};
			}
		}
		throw new IllegalArgumentException(String.format("Cannot find type for %s//%s", resourceType, componentType));
	}

	private Class<?> clazz(final String resourceType,
			final String componentType)
	{
		switch (resourceType) {
		case "syntax":
			switch (componentType) {
			case "node":
				return SyntaxNode.class;
			case "token":
				return SyntaxToken.class;
			}
		}
		throw new IllegalArgumentException(String.format("Cannot find type for %s//%s", resourceType, componentType));
	}

	@Override
	public void verifyDataType(final String resourceType, final String componentType, final Object data)
	{
		checkNotNull(data);
		clazz(resourceType, componentType).cast(data);
	}

	@Override
	public boolean componentTypeExists(final String resourceType, final String componentType)
	{
		switch (resourceType) {
		case "syntax":
			switch (componentType) {
			case "node":
			case "token":
				return true;
			}
		}
		return false;
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
