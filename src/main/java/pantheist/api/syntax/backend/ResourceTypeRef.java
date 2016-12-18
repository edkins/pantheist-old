package pantheist.api.syntax.backend;

import pantheist.api.syntax.model.ListResourceResponse;

/**
 * Represents a handle to a particular resource type, e.g. /syntax.
 *
 * The datastore will only be locked when you make one of the atomic read or
 * write operations on it, not when you call .resource() etc.
 */
public interface ResourceTypeRef
{
	/**
	 * Return a reference to the resource with the given id.
	 *
	 * @param resourceId
	 *            resource identifier
	 * @return reference to that resource
	 */
	ResourceRef resource(String resourceId);

	/**
	 * Returns a sorted immutable list containing resource IDs for this resource
	 * type.
	 *
	 * The resource IDs can be fed to resource() but note that this will no
	 * longer be an atomic operation.
	 *
	 * @return list of resource IDs.
	 */
	ListResourceResponse listResources();

	/**
	 * @return the path, e.g. /syntax
	 */
	String path();
}
