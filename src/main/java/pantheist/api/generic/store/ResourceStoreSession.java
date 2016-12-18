package pantheist.api.generic.store;

import java.io.Closeable;
import java.util.List;

import pantheist.common.except.AlreadyPresentException;
import pantheist.common.except.NotFoundException;

/**
 * Represents a view on the resource store. The store is locked while this is
 * open, so do make sure to close it when you're finished.
 *
 * The current implementation uses a bunch of files. These will be loaded into
 * memory when you ask for them, and stored back to disk when the session is
 * closed.
 */
public interface ResourceStoreSession extends Closeable
{
	List<String> listResources(String resourceType);

	/**
	 * @param resourceType
	 * @param resourceId
	 * @return
	 * @throws NotFoundException
	 * @throws ResourceStoreException
	 *             if there's an IO problem
	 */
	OpenResource resource(String resourceType, String resourceId) throws NotFoundException;

	void createEmptyResource(String resourceType, String resourceId) throws AlreadyPresentException;

	/**
	 * Hides any IOException because they're annoying
	 *
	 * @throws ResourceStoreException
	 *             if there's an IO problem
	 */
	@Override
	void close();

	void deleteResource(String resourceType, String resourceId) throws NotFoundException;
}
