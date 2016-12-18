package pantheist.api.syntax.backend;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import pantheist.api.syntax.model.SyntaxModelFactory;
import pantheist.common.except.AlreadyPresentException;
import pantheist.common.except.NotFoundException;

final class ResourceRefImpl implements ResourceRef
{
	private final ResourceTypeRefImpl resourceType;
	private final String resourceId;

	ResourceRefImpl(final ResourceTypeRefImpl resourceType, final String resourceId)
	{
		this.resourceType = checkNotNull(resourceType);
		this.resourceId = checkNotNullOrEmpty(resourceId);
	}

	@Override
	public ComponentCollectionRef componentType(final String componentTypeId) throws NotFoundException
	{
		checkNotNullOrEmpty(componentTypeId);
		if (!resourceType.listComponentTypeIds().contains(componentTypeId))
		{
			throw new NotFoundException(componentTypePath(componentTypeId));
		}
		return new ComponentCollectionRefImpl(this, componentTypeId);
	}

	@Override
	public void create() throws AlreadyPresentException
	{
		synchronized (root())
		{
			if (resourceType.shareMap().containsKey(resourceId))
			{
				throw new AlreadyPresentException(path());
			}

			// Initialize each component type to an empty map.
			final HashMap<String, SortedMap<String, Object>> map = new HashMap<>();
			for (final String componentTypeId : resourceType.listComponentTypeIds())
			{
				map.put(componentTypeId, new TreeMap<>());
			}
			resourceType.shareMap().put(resourceId, map);
		}
	}

	@Override
	public void delete() throws NotFoundException
	{
		synchronized (root())
		{
			if (resourceType.shareMap().remove(resourceId) == null)
			{
				throw new NotFoundException(path());
			}
		}
	}

	@Override
	public String path()
	{
		return resourceType.resourcePath(resourceId);
	}

	String componentTypePath(final String componentTypeId)
	{
		return path() + "/" + componentTypeId;
	}

	Object root()
	{
		return resourceType.root();
	}

	SyntaxModelFactory modelFactory()
	{
		return resourceType.modelFactory();
	}

	Map<String, SortedMap<String, Object>> shareMap() throws NotFoundException
	{
		final Map<String, SortedMap<String, Object>> result = resourceType.shareMap().get(resourceId);
		if (result == null)
		{
			throw new NotFoundException(path());
		}
		return result;
	}

	void checkDataType(final String componentTypeId, final Object data)
	{
		resourceType.checkDataType(componentTypeId, data);
	}
}
