package pantheist.api.generic.schema;

import com.fasterxml.jackson.core.type.TypeReference;

import pantheist.api.generic.store.OpenResource;
import pantheist.api.syntax.model.PutComponentRequest;

/**
 * It knows what types different components are supposed to be.
 */
public interface TypeKnower
{
	TypeReference<? extends PutComponentRequest<?>> putRequestTypeRef(String resourceType, String componentType);

	/**
	 * @param resourceType
	 * @param componentType
	 * @param data
	 * @throws Some
	 *             kind of runtime exception if the type is wrong or if data is
	 *             null.
	 */
	void verifyDataType(String resourceType, String componentType, Object data);

	/**
	 * Returns whether the component type exists at all.
	 *
	 * @param resourceType
	 * @param componentType
	 * @return
	 */
	boolean componentTypeExists(String resourceType, String componentType);

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
