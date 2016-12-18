package pantheist.api.generic.store;

import java.util.concurrent.Semaphore;

interface ResourceStoreFactory
{
	ResourceStoreSession session(Semaphore semaphore);
}
