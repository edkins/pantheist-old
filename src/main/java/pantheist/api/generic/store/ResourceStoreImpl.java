package pantheist.api.generic.store;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.Semaphore;

import javax.inject.Inject;

final class ResourceStoreImpl implements ResourceStore
{
	private final ResourceStoreFactory factory;

	// State
	private final Semaphore semaphore;

	@Inject
	ResourceStoreImpl(final ResourceStoreFactory factory)
	{
		this.factory = checkNotNull(factory);

		this.semaphore = new Semaphore(1);
	}

	@Override
	public ResourceStoreSession open()
	{
		try
		{
			semaphore.acquire();
			return factory.session(semaphore);
		}
		catch (final InterruptedException e)
		{
			throw new ResourceStoreException(e);
		}
	}

}
