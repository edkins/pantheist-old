package pantheist.api.generic.store;

/**
 * Represents the resource store. Can only be accessed by opening a session,
 * making a quick change and then closing it.
 */
public interface ResourceStore
{
	/**
	 * Must close.
	 *
	 * @return an open session
	 * @throws ResourceStoreException
	 *             if the thread is interrupted while acquiring the mutex
	 */
	ResourceStoreSession open();
}
