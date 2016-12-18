package pantheist.api.syntax.backend;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.stream.Collectors;

import com.google.common.base.Throwables;

import pantheist.api.syntax.model.ListComponentResponse;
import pantheist.api.syntax.model.ListedComponent;
import pantheist.common.except.NotFoundException;

final class ComponentCollectionRefImpl implements ComponentCollectionRef
{
	private final ResourceRefImpl resource;
	private final String componentTypeId;

	ComponentCollectionRefImpl(final ResourceRefImpl resource, final String componentTypeId)
	{
		this.resource = checkNotNull(resource);
		this.componentTypeId = checkNotNullOrEmpty(componentTypeId);
	}

	@Override
	public ComponentRef component(final String componentId)
	{
		return new ComponentRefImpl(this, componentId);
	}

	@Override
	public ListComponentResponse listComponents() throws NotFoundException
	{
		synchronized (root())
		{
			final List<ListedComponent> components = shareMap()
					.entrySet()
					.stream()
					.map(this::toListedComponent)
					.collect(Collectors.toList());
			return resource.modelFactory().listComponentResponse(components);
		}
	}

	private ListedComponent toListedComponent(final Entry<String, Object> entry)
	{
		final String componentId = entry.getKey();
		final Object data = entry.getValue();
		final String path = componentPath(componentId);
		return resource.modelFactory().listedComponent(path, componentId, data);
	}

	@Override
	public String path()
	{
		return resource.componentTypePath(componentTypeId);
	}

	String componentPath(final String componentId)
	{
		try
		{
			return path() + "/" + URLEncoder.encode(componentId, "utf-8");
		}
		catch (final UnsupportedEncodingException e)
		{
			throw Throwables.propagate(e);
		}
	}

	SortedMap<String, Object> shareMap() throws NotFoundException
	{
		return resource.shareMap().get(componentTypeId);
	}

	Object root()
	{
		return resource.root();
	}

	void checkDataType(final Object data)
	{
		resource.checkDataType(componentTypeId, data);
	}
}
