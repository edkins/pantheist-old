package pantheist.api.syntax.backend;

import pantheist.api.syntax.model.ListComponentResponse;
import pantheist.api.syntax.model.ListSyntaxResponse;
import pantheist.api.syntax.model.PutComponentRequest;
import pantheist.common.except.AlreadyPresentException;
import pantheist.common.except.NotFoundException;

public interface SyntaxBackend
{
	/**
	 * @return a list of all the known syntax structures, sorted alphabetically.
	 */
	ListSyntaxResponse listSyntax();

	/**
	 * Creates a new empty syntax object with the given identifier.
	 *
	 * @param id
	 * @throws AlreadyPresentException
	 *             if the given named resource already exists
	 */
	void createSyntax(String id) throws AlreadyPresentException;

	/**
	 * Deletes the syntax object with the given identifier.
	 *
	 * @param id
	 * @throws NotFoundException
	 *             if no resource of the correct type exists with that
	 *             identifier.
	 */
	void deleteSyntax(String id) throws NotFoundException;

	/**
	 * List the components of a particular component type associated with a
	 * syntax resource.
	 *
	 * @param syntaxId
	 *            Identifies the syntax resource
	 * @param componentType
	 *            the component type we want to list
	 * @return list of components
	 * @throws NotFoundException
	 *             if there is no syntax resource with that id
	 */
	ListComponentResponse listComponents(String syntaxId, ComponentType componentType) throws NotFoundException;

	/**
	 * Retrieve a particular component.
	 *
	 * @param syntaxId
	 *            Identifies the syntax resource
	 * @param componentType
	 *            the component type
	 * @param componentId
	 *            Identifies the component within the syntax resource
	 * @return A node object
	 * @throws NotFoundException
	 *             if there is no syntax resource with that id, or no node with
	 *             that id within the syntax resource
	 */
	Object getComponent(String syntaxId, ComponentType componentType, String componentId) throws NotFoundException;

	/**
	 * Creates or updates the given component.
	 *
	 * @param syntaxId
	 *            Identifies the syntax resource
	 * @param componentType
	 *            the component type
	 * @param componentId
	 *            Identifies the component within the syntax resource
	 * @param request
	 *            Contains additional information needed to create the component
	 * @throws NotFoundException
	 *             if there is no syntax resource with that id, or if there is
	 *             no node with that id and the put request specifies to update
	 *             an existing node
	 * @throws AlreadyPresentException
	 *             if there is already a node with that id, and the put request
	 *             does not permit overwriting it
	 */
	<T> void putComponent(String syntaxId, ComponentType componentType, String componentId,
			PutComponentRequest<T> request)
			throws NotFoundException, AlreadyPresentException;

	/**
	 * Delete the given node from the given syntax resource.
	 *
	 * @param syntaxId
	 *            Identifies the syntax resource
	 * @param componentType
	 *            the component type
	 * @param componentId
	 *            Identifies the component within the syntax resource
	 * @throws NotFoundException
	 *             if either the syntax resource or the node does not exist.
	 */
	void deleteComponent(String syntaxId, ComponentType componentType, String componentId) throws NotFoundException;
}
