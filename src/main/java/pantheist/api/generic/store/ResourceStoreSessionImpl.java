package pantheist.api.generic.store;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;

import pantheist.api.generic.schema.TypeKnower;
import pantheist.common.except.AlreadyPresentException;
import pantheist.common.except.NotFoundException;
import pantheist.system.config.PantheistConfig;

final class ResourceStoreSessionImpl implements ResourceStoreSession
{
	private final PantheistConfig config;
	private final ObjectMapper objectMapper;

	// State
	private final Map<ResourceIdWithType, OpenResource> openResources;
	private final Set<ResourceIdWithType> toDelete;
	private final Semaphore semaphore;
	private final TypeKnower typeKnower;

	@Inject
	ResourceStoreSessionImpl(final PantheistConfig config,
			final ObjectMapper objectMapper,
			final TypeKnower typeKnower,
			@Assisted final Semaphore semaphore)
	{
		this.config = checkNotNull(config);
		this.objectMapper = checkNotNull(objectMapper);
		this.typeKnower = checkNotNull(typeKnower);
		this.semaphore = checkNotNull(semaphore);
		this.openResources = new HashMap<>();
		this.toDelete = new HashSet<>();
	}

	private File typePath(final String resourceType)
	{
		return new File(config.dataPath(), resourceType);
	}

	private File path(final ResourceIdWithType id)
	{
		return new File(typePath(id.resourceType), id.sanitizedResourceId());
	}

	@Override
	public void close()
	{
		try
		{
			for (final Entry<ResourceIdWithType, OpenResource> e : openResources.entrySet())
			{
				final File file = path(e.getKey());
				if (!file.getParentFile().isDirectory())
				{
					file.getParentFile().mkdirs();
				}
				try (OutputStream output = new FileOutputStream(file))
				{
					objectMapper.writeValue(output, e.getValue());
				}
			}
			for (final ResourceIdWithType id : toDelete)
			{
				path(id).delete();
			}
		}
		catch (final IOException e)
		{
			throw new ResourceStoreException(e);
		}
		finally
		{
			semaphore.release();
		}
	}

	@Override
	public OpenResource resource(final String resourceType, final String resourceId) throws NotFoundException
	{
		try
		{
			final ResourceIdWithType id = new ResourceIdWithType(resourceType, resourceId);
			if (!exists(id))
			{
				throw new NotFoundException(resourceId);
			}
			if (!openResources.containsKey(id))
			{
				try (InputStream input = new FileInputStream(path(id)))
				{
					final Class<? extends OpenResource> clazz = typeKnower.resourceClass(resourceType);
					final OpenResource resource = objectMapper.readValue(input, clazz);
					openResources.put(id, resource);
				}
			}
			return openResources.get(id);
		}
		catch (final IOException e)
		{
			throw new ResourceStoreException(e);
		}
	}

	@Override
	public List<String> listResources(final String resourceType)
	{
		final File path = typePath(resourceType);
		final SortedSet<String> result = new TreeSet<>();
		if (path.isDirectory())
		{
			for (final File file : path.listFiles())
			{
				result.add(file.getName());
			}
		}
		for (final ResourceIdWithType id : openResources.keySet())
		{
			if (id.resourceType.equals(resourceType))
			{
				result.add(id.resourceId);
			}
		}
		for (final ResourceIdWithType id : toDelete)
		{
			if (id.resourceType.equals(resourceType))
			{
				result.remove(id.resourceId);
			}
		}
		return ImmutableList.copyOf(result);
	}

	private boolean exists(final ResourceIdWithType id)
	{
		if (toDelete.contains(id))
		{
			return false;
		}
		if (openResources.containsKey(id))
		{
			return true;
		}
		return path(id).exists();
	}

	@Override
	public void createEmptyResource(final String resourceType, final String resourceId) throws AlreadyPresentException
	{
		final ResourceIdWithType id = new ResourceIdWithType(resourceType, resourceId);
		if (!typeKnower.resourceTypeExists(resourceType))
		{
			// unlike the component types, we shouldn't ever receive requests with the wrong resource type
			// so throw IllegalArgumentException instead of NotFoundException.
			throw new IllegalArgumentException("Bad resource type " + resourceType);
		}
		if (exists(id))
		{
			throw new AlreadyPresentException(resourceId);
		}
		toDelete.remove(id);
		openResources.put(id, typeKnower.createEmptyResource(resourceType));
	}

	@Override
	public void deleteResource(final String resourceType, final String resourceId) throws NotFoundException
	{
		final ResourceIdWithType id = new ResourceIdWithType(resourceType, resourceId);
		if (!exists(id))
		{
			throw new NotFoundException(resourceId);
		}
		toDelete.add(id);
		openResources.remove(id);
	}

}
