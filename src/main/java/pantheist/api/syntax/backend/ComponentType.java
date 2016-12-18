package pantheist.api.syntax.backend;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.fasterxml.jackson.core.type.TypeReference;

import pantheist.api.syntax.model.PutComponentRequest;
import pantheist.api.syntax.model.SyntaxNode;
import pantheist.api.syntax.model.SyntaxToken;

public enum ComponentType
{
	node(SyntaxNode.class, new TypeReference<PutComponentRequest<SyntaxNode>>() {
	}), token(SyntaxToken.class, new TypeReference<PutComponentRequest<SyntaxToken>>() {
	});

	private final Class<?> associatedType;
	private final TypeReference<? extends PutComponentRequest<?>> putRequestTypeRef;

	private ComponentType(final Class<?> associatedType,
			final TypeReference<? extends PutComponentRequest<?>> putRequestTypeRef)
	{
		this.associatedType = checkNotNull(associatedType);
		this.putRequestTypeRef = checkNotNull(putRequestTypeRef);
	}

	public void verifyType(final Object o)
	{
		checkNotNull(o);
		try
		{
			associatedType.cast(o);
		}
		catch (final ClassCastException e)
		{
			throw new IllegalArgumentException("Object is the wrong type", e);
		}
	}

	public TypeReference<? extends PutComponentRequest<?>> putRequestTypeRef()
	{
		return putRequestTypeRef;
	}

	public static <T> Map<ComponentType, T> emptyMap(final Supplier<T> empty)
	{
		final Map<ComponentType, T> result = new HashMap<>();
		for (final ComponentType ct : ComponentType.values())
		{
			result.put(ct, empty.get());
		}
		return result;
	}
}
