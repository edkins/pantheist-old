package pantheist.api.generic.schema;

import pantheist.api.generic.store.OpenResource;

/**
 * It knows what types different components are supposed to be.
 */
public interface TypeKnower
{
	/**
	 * Returns whether the resource type is valid.
	 *
	 * @param resourceType
	 * @return
	 */
	boolean resourceTypeExists(String resourceType);

	/**
	 * Return the java type associated with the given resource type. It will be
	 * suitable for json deserialization and will implement the OpenResource
	 * interface.
	 *
	 * @param resourceType
	 * @return
	 */
	Class<? extends OpenResource> resourceClass(String resourceType);

	/**
	 * Create an empty resource of the given type.
	 *
	 * @param resourceType
	 * @return
	 */
	OpenResource createEmptyResource(String resourceType);
};
