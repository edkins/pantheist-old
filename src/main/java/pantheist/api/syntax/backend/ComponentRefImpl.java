package pantheist.api.syntax.backend;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.Map;

import pantheist.common.except.AlreadyPresentException;
import pantheist.common.except.NotFoundException;

final class ComponentRefImpl implements ComponentRef
{
	private final ComponentCollectionRefImpl componentCollection;
	private final String componentId;

	ComponentRefImpl(final ComponentCollectionRefImpl componentCollection, final String componentId)
	{
		this.componentCollection = checkNotNull(componentCollection);
		this.componentId = checkNotNullOrEmpty(componentId);
	}

	@Override
	public void delete() throws NotFoundException
	{
		synchronized (componentCollection.root())
		{
			if (componentCollection.shareMap().remove(componentId) == null)
			{
				throw new NotFoundException(path());
			}
		}
	}

	@Override
	public void create(final Object data) throws AlreadyPresentException, NotFoundException
	{
		checkNotNull(data);
		componentCollection.checkDataType(data);
		synchronized (componentCollection.root())
		{
			final Map<String, Object> map = componentCollection.shareMap();
			if (map.containsKey(componentId))
			{
				throw new AlreadyPresentException(path());
			}
			map.put(componentId, data);
		}
	}

	@Override
	public Object getData() throws NotFoundException
	{
		synchronized (componentCollection.root())
		{
			final Object result = componentCollection.shareMap().get(componentId);
			if (result == null)
			{
				throw new NotFoundException(path());
			}
			return result;
		}
	}

	@Override
	public String path()
	{
		return componentCollection.componentPath(componentId);
	}

}
