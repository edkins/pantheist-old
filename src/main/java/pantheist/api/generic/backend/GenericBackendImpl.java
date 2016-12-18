package pantheist.api.generic.backend;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

import pantheist.api.generic.model.ApiGenericModelFactory;
import pantheist.api.generic.model.ListComponentResponse;
import pantheist.api.generic.model.ListResourceResponse;
import pantheist.api.generic.model.ListedComponent;
import pantheist.api.generic.model.ResourceMetadata;
import pantheist.api.generic.schema.TypeKnower;
import pantheist.api.generic.store.ResourceStore;
import pantheist.api.generic.store.ResourceStoreSession;
import pantheist.api.syntax.model.IntolerantMap;
import pantheist.api.syntax.model.SyntaxNode;
import pantheist.api.syntax.model.SyntaxToken;
import pantheist.common.except.AlreadyPresentException;
import pantheist.common.except.NotFoundException;

final class GenericBackendImpl implements GenericBackend
{
	private final ApiGenericModelFactory modelFactory;
	private final ResourceStore store;
	private final TypeKnower typeKnower;

	@Inject
	GenericBackendImpl(final ApiGenericModelFactory modelFactory, final ResourceStore store,
			final TypeKnower typeKnower)
	{
		this.modelFactory = checkNotNull(modelFactory);
		this.store = checkNotNull(store);
		this.typeKnower = checkNotNull(typeKnower);
	}

	void checkDataType(final String resourceTypeId, final String componentTypeId, final Object data)
	{
		boolean ok = false;
		switch (resourceTypeId) {
		case "syntax":
			ok = checkSyntaxDataType(componentTypeId, data);
			break;
		}

		if (!ok)
		{
			throw new IllegalArgumentException(String.format("Data type %s is incorrect for %s//%s",
					data.getClass().getSimpleName(), resourceTypeId, componentTypeId));
		}
	}

	private boolean checkSyntaxDataType(final String componentTypeId, final Object data)
	{
		switch (componentTypeId) {
		case "node":
			return data instanceof SyntaxNode;
		case "token":
			return data instanceof SyntaxToken;
		default:
			return false;
		}
	}

	@Override
	public ListResourceResponse listResources(final String resourceType)
	{
		return new RType(resourceType).listResources();
	}

	private class RType
	{
		private final String resourceType;

		RType(final String resourceType)
		{
			this.resourceType = checkNotNullOrEmpty(resourceType);
		}

		ListResourceResponse listResources()
		{
			try (ResourceStoreSession session = store.open())
			{
				final List<ResourceMetadata> list = Lists.transform(session.listResources(resourceType),
						id -> new Res(id).getMetadata());
				return modelFactory.listResourceResponse(list);
			}
		}

		String resourceTypePath()
		{
			return "/" + resourceType;
		}

		private class Res
		{
			private final String resourceId;

			Res(final String resourceId)
			{
				this.resourceId = checkNotNullOrEmpty(resourceId);
			}

			private ResourceMetadata getMetadata()
			{
				return modelFactory.resourceMetadata(resourcePath(), resourceId, resourceId);
			}

			private String resourcePath()
			{
				try
				{
					return resourceTypePath() + "/" + URLEncoder.encode(resourceId, "utf-8");
				}
				catch (final UnsupportedEncodingException e)
				{
					throw Throwables.propagate(e);
				}
			}

			void create() throws AlreadyPresentException
			{
				try (ResourceStoreSession session = store.open())
				{
					session.createEmptyResource(resourceType, resourceId);
				}
			}

			void delete() throws NotFoundException
			{
				try (ResourceStoreSession session = store.open())
				{
					session.deleteResource(resourceType, resourceId);
				}
			}

			class CType
			{
				private final String componentType;

				CType(final String componentType)
				{
					this.componentType = checkNotNullOrEmpty(componentType);
				}

				ListComponentResponse list() throws NotFoundException
				{
					try (ResourceStoreSession session = store.open())
					{
						final List<ListedComponent> list = session.resource(resourceType, resourceId)
								.components(componentType)
								.sortedEntryList()
								.stream()
								.map(this::toListedComponent)
								.collect(Collectors.toList());

						return modelFactory.listComponentResponse(list);
					}
				}

				private ListedComponent toListedComponent(final Entry<String, Object> entry)
				{
					final String componentId = entry.getKey();
					final Object data = entry.getValue();
					final String path = new Comp(componentId).componentPath();
					return modelFactory.listedComponent(path, componentId, data);
				}

				String componentTypePath()
				{
					return resourcePath() + "/" + componentType;
				}

				class Comp
				{
					private final String componentId;

					Comp(final String componentId)
					{
						this.componentId = checkNotNullOrEmpty(componentId);
					}

					String componentPath()
					{
						try
						{
							return componentTypePath() + "/" + URLEncoder.encode(componentId, "utf-8");
						}
						catch (final UnsupportedEncodingException e)
						{
							throw Throwables.propagate(e);
						}

					}

					Object getData() throws NotFoundException
					{
						try (ResourceStoreSession session = store.open())
						{
							final Object result = session.resource(resourceType, resourceId).components(componentType)
									.get(componentId);
							if (result == null)
							{
								throw new NotFoundException(componentPath());
							}
							return result;
						}
					}

					void createWithData(final Object data) throws AlreadyPresentException, NotFoundException
					{
						try (ResourceStoreSession session = store.open())
						{
							final IntolerantMap map = session.resource(resourceType, resourceId)
									.components(componentType);
							if (map.containsKey(componentId))
							{
								throw new AlreadyPresentException(componentPath());
							}
							map.put(componentId, data);
						}
					}

					void delete() throws NotFoundException
					{
						try (ResourceStoreSession session = store.open())
						{
							final IntolerantMap map = session.resource(resourceType, resourceId)
									.components(componentType);
							if (map.remove(componentId) == null)
							{
								throw new NotFoundException(componentPath());
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void createEmptyResource(final String resourceType, final String resourceId) throws AlreadyPresentException
	{
		new RType(resourceType).new Res(resourceId).create();
	}

	@Override
	public void deleteResource(final String resourceType, final String resourceId) throws NotFoundException
	{
		new RType(resourceType).new Res(resourceId).delete();
	}

	@Override
	public ListComponentResponse listComponents(final String resourceType, final String resourceId,
			final String componentType) throws NotFoundException
	{
		return new RType(resourceType).new Res(resourceId).new CType(componentType).list();
	}

	@Override
	public Object getComponent(final String resourceType, final String resourceId, final String componentType,
			final String componentId) throws NotFoundException
	{
		return new RType(resourceType).new Res(resourceId).new CType(componentType).new Comp(componentId).getData();
	}

	@Override
	public void createComponent(final String resourceType, final String resourceId, final String componentType,
			final String componentId,
			final Object data) throws NotFoundException, AlreadyPresentException
	{
		new RType(resourceType).new Res(resourceId).new CType(componentType).new Comp(componentId)
				.createWithData(data);
	}

	@Override
	public void deleteComponent(final String resourceType, final String resourceId, final String componentType,
			final String componentId)
			throws NotFoundException
	{
		new RType(resourceType).new Res(resourceId).new CType(componentType).new Comp(componentId).delete();
	}
}
