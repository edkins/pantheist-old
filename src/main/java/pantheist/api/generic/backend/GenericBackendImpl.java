package pantheist.api.generic.backend;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.common.base.Throwables;

import pantheist.api.generic.model.ApiGenericModelFactory;
import pantheist.api.generic.model.ListComponentResponse;
import pantheist.api.generic.model.ListResourceResponse;
import pantheist.api.generic.model.ListedComponent;
import pantheist.api.generic.model.ResourceMetadata;
import pantheist.api.generic.model.TypedMap;
import pantheist.api.generic.store.ResourceStore;
import pantheist.api.generic.store.ResourceStoreSession;
import pantheist.common.except.AlreadyPresentException;
import pantheist.common.except.InvalidLocationException;
import pantheist.common.except.NotFoundException;

final class GenericBackendImpl implements GenericBackend
{
	private final ApiGenericModelFactory modelFactory;
	private final ResourceStore store;

	@Inject
	GenericBackendImpl(final ApiGenericModelFactory modelFactory, final ResourceStore store)
	{
		this.modelFactory = checkNotNull(modelFactory);
		this.store = checkNotNull(store);
	}

	private String resourcePath(final String resourceType, final String resourceId)
	{
		try
		{
			return new StringBuilder()
					.append(resourceType)
					.append("/")
					.append(URLEncoder.encode(resourceId, "utf-8"))
					.toString();
		}
		catch (final UnsupportedEncodingException e)
		{
			throw Throwables.propagate(e);
		}
	}

	private String componentPath(final String resourceType, final String resourceId,
			final String componentType, final String componentId)
	{
		try
		{
			return new StringBuilder()
					.append(resourceType)
					.append("/")
					.append(URLEncoder.encode(resourceId, "utf-8"))
					.append("/")
					.append(componentType)
					.append(URLEncoder.encode(componentId, "utf-8"))
					.toString();
		}
		catch (final UnsupportedEncodingException e)
		{
			throw Throwables.propagate(e);
		}
	}

	@Override
	public ListResourceResponse listResources(final String resourceType)
	{
		try (ResourceStoreSession session = store.open())
		{
			final List<ResourceMetadata> list = session.listResources(resourceType)
					.stream()
					.map(resourceId -> {
						return modelFactory.resourceMetadata(
								resourcePath(resourceType, resourceId),
								resourceId,
								resourceId);
					})
					.collect(Collectors.toList());
			return modelFactory.listResourceResponse(list);
		}
	}

	@Override
	public void createEmptyResource(final String resourceType, final String resourceId) throws AlreadyPresentException
	{
		try (ResourceStoreSession session = store.open())
		{
			session.createEmptyResource(resourceType, resourceId);
		}
	}

	@Override
	public void deleteResource(final String resourceType, final String resourceId) throws NotFoundException
	{
		try (ResourceStoreSession session = store.open())
		{
			session.deleteResource(resourceType, resourceId);
		}
	}

	@Override
	public ListComponentResponse listComponents(final String resourceType, final String resourceId,
			final String componentType) throws NotFoundException
	{
		try (ResourceStoreSession session = store.open())
		{
			final List<ListedComponent> list = session.resource(resourceType, resourceId)
					.components(componentType)
					.sortedEntryList()
					.stream()
					.map(e -> {
						final String componentId = e.getKey();
						final String path = componentPath(resourceType, resourceId, componentType, componentId);
						return modelFactory.listedComponent(path, componentId, e.getValue());
					})
					.collect(Collectors.toList());

			return modelFactory.listComponentResponse(list);
		}
	}

	@Override
	public Object getComponent(final String resourceType, final String resourceId, final String componentType,
			final String componentId) throws NotFoundException
	{
		try (ResourceStoreSession session = store.open())
		{
			final Object result = session.resource(resourceType, resourceId).components(componentType)
					.get(componentId);
			if (result == null)
			{
				throw new NotFoundException(componentId);
			}
			return result;
		}
	}

	@Override
	public void createComponent(final String resourceType, final String resourceId, final String componentType,
			final String componentId,
			final Object data) throws NotFoundException, AlreadyPresentException
	{
		try (ResourceStoreSession session = store.open())
		{
			final TypedMap map = session.resource(resourceType, resourceId)
					.components(componentType);
			if (map.containsKey(componentId))
			{
				throw new AlreadyPresentException(componentId);
			}
			map.put(componentId, data);
		}
	}

	@Override
	public void deleteComponent(final String resourceType, final String resourceId, final String componentType,
			final String componentId)
			throws NotFoundException
	{
		try (ResourceStoreSession session = store.open())
		{
			final TypedMap map = session.resource(resourceType, resourceId)
					.components(componentType);
			if (map.remove(componentId) == null)
			{
				throw new NotFoundException(componentId);
			}
		}
	}

	@Override
	public Class<?> desiredComponentType(final String resourceType, final String resourceId, final String componentType,
			final String componentId) throws NotFoundException, InvalidLocationException
	{
		try (final ResourceStoreSession session = store.open())
		{
			return session.resource(resourceType, resourceId)
					.components(componentType)
					.typeOf(componentId)
					.orElseThrow(() -> new InvalidLocationException(componentId));
		}
	}
}
