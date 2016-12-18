package pantheist.api.syntax.backend;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

import com.google.common.base.Throwables;

import pantheist.api.syntax.model.ListResourceResponse;
import pantheist.api.syntax.model.ResourceMetadata;
import pantheist.api.syntax.model.SyntaxModelFactory;

final class ResourceTypeRefImpl implements ResourceTypeRef
{
	private final SyntaxBackendImpl backend;
	private final String resourceTypeId;

	ResourceTypeRefImpl(final SyntaxBackendImpl backend, final String resourceTypeId)
	{
		this.backend = checkNotNull(backend);
		this.resourceTypeId = checkNotNullOrEmpty(resourceTypeId);
	}

	@Override
	public ResourceRef resource(final String resourceId)
	{
		checkNotNullOrEmpty(resourceId);
		return new ResourceRefImpl(this, resourceId);
	}

	@Override
	public ListResourceResponse listResources()
	{
		synchronized (root())
		{
			final List<ResourceMetadata> list = shareMap().keySet()
					.stream()
					.map(this::getMetadata)
					.collect(Collectors.toList());
			return modelFactory().listResourceResponse(list);

		}
	}

	private ResourceMetadata getMetadata(final String resourceId)
	{
		return modelFactory().resourceMetadata(resourcePath(resourceId), resourceId, resourceId);
	}

	String resourcePath(final String resourceId)
	{
		try
		{
			return path() + "/" + URLEncoder.encode(resourceId, "utf-8");
		}
		catch (final UnsupportedEncodingException e)
		{
			throw Throwables.propagate(e);
		}
	}

	@Override
	public String path()
	{
		return "/" + resourceTypeId;
	}

	Object root()
	{
		return backend;
	}

	void checkDataType(final String componentTypeId, final Object data)
	{
		backend.checkDataType(resourceTypeId, componentTypeId, data);
	}

	SyntaxModelFactory modelFactory()
	{
		return backend.modelFactory();
	}

	Collection<String> listComponentTypeIds()
	{
		return backend.listComponentTypeIds(resourceTypeId);
	}

	public SortedMap<String, Map<String, SortedMap<String, Object>>> shareMap()
	{
		return backend.shareMap().get(resourceTypeId);
	}

}
