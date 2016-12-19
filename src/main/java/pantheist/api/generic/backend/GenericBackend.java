package pantheist.api.generic.backend;

import pantheist.api.generic.model.ListComponentResponse;
import pantheist.api.generic.model.ListResourceResponse;
import pantheist.common.except.AlreadyPresentException;
import pantheist.common.except.InvalidLocationException;
import pantheist.common.except.NotFoundException;

public interface GenericBackend
{
	/**
	 * List the resources of a particular type.
	 *
	 * @param string
	 *            the resource type
	 * @return a response containing resource ID's
	 */
	ListResourceResponse listResources(String resourceType);

	/**
	 * Create a new empty resource of the given type with the given ID. Fails if
	 * it already exists.
	 *
	 * @param resourceType
	 *            the resource type
	 * @param resourceId
	 *            identifier for the new resource
	 * @throws AlreadyPresentException
	 *             if the resource already exists
	 */
	void createEmptyResource(String resourceType, String resourceId) throws AlreadyPresentException;

	/**
	 * Delete a resource with the given ID.
	 *
	 * @param resourceType
	 *            the resource type
	 * @param resourceId
	 *            identifier for the resource to delete
	 * @throws NotFoundException
	 *             if the resource does not exist
	 */
	void deleteResource(String resourceType, String resourceId) throws NotFoundException;

	/**
	 * List the components of a particular component type.
	 *
	 * @param resourceType
	 *            the resource type
	 * @param resourceId
	 *            the resource identifier
	 * @param componentType
	 *            the component type
	 * @return a response containing component ID's
	 * @throws NotFoundException
	 *             if the resource does not exist
	 */
	ListComponentResponse listComponents(final String resourceType, final String resourceId, final String componentType)
			throws NotFoundException;

	/**
	 * Returns a component. The type will depend on componentType.
	 *
	 * @param resourceType
	 *            the resource type
	 * @param resourceId
	 *            the resource identifier
	 * @param componentType
	 *            the component type
	 * @param componentId
	 *            the component identifer
	 * @return the component data
	 * @throws NotFoundException
	 */
	Object getComponent(String resourceType, String resourceId, String componentType, String componentId)
			throws NotFoundException;

	/**
	 * Create a component. The type of data must agree with componentType.
	 *
	 * @param resourceType
	 * @param resourceId
	 * @param componentType
	 * @param componentId
	 * @param data
	 * @throws NotFoundException
	 *             if the resource is not found
	 * @throws AlreadyPresentException
	 *             if the component already exists
	 */
	void createComponent(String resourceType, String resourceId, String componentType, String componentId, Object data)
			throws NotFoundException, AlreadyPresentException;

	/**
	 * Returns the desired java type for components stored at the given
	 * location.
	 *
	 * @param resourceType
	 * @param resourceId
	 * @param componentType
	 * @param componentId
	 * @return
	 * @throws NotFoundException
	 *             if the resource does not exist or the compnentType is invalid
	 *             for the resource
	 * @throws InvalidLocationException
	 *             if the componentId is invalid for that componentType.
	 */
	Class<?> desiredComponentType(String resourceType, String resourceId, String componentType, String componentId)
			throws NotFoundException, InvalidLocationException;

	/**
	 * Delete a component
	 *
	 * @param resourceType
	 * @param resourceId
	 * @param componentType
	 * @param componentId
	 * @throws NotFoundException
	 *             if either the resource or the component does not exist
	 */
	void deleteComponent(String resourceType, String resourceId, String componentType, String componentId)
			throws NotFoundException;
}
