package pantheist.api.syntax.backend;

import pantheist.api.syntax.model.ListNodeResponse;
import pantheist.api.syntax.model.ListSyntaxResponse;
import pantheist.api.syntax.model.PutNodeRequest;
import pantheist.api.syntax.model.SyntaxNode;
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
	 * List the nodes associated with a syntax resource.
	 *
	 * @param syntaxId
	 *            Identifies the syntax resource
	 * @return list of nodes
	 * @throws NotFoundException
	 *             if there is no syntax resource with that id
	 */
	ListNodeResponse listNodes(String syntaxId) throws NotFoundException;

	/**
	 * Retrieve a particular node.
	 *
	 * @param syntaxId
	 *            Identifies the syntax resource
	 * @param nodeId
	 *            Identifies the node within the syntax resource
	 * @return A node object
	 * @throws NotFoundException
	 *             if there is no syntax resource with that id, or no node with
	 *             that id within the syntax resource
	 */
	SyntaxNode getNode(String syntaxId, String nodeId) throws NotFoundException;

	/**
	 * Creates or updates the given node.
	 *
	 * @param syntaxId
	 *            Identifies the syntax resource
	 * @param nodeId
	 *            Identifies the node within the syntax resource
	 * @param node
	 *            Contains additional information needed to create the node
	 * @throws NotFoundException
	 *             if there is no syntax resource with that id, or if there is
	 *             no node with that id and the put request specifies to update
	 *             an existing node
	 * @throws AlreadyPresentException
	 *             if there is already a node with that id, and the put request
	 *             does not permit overwriting it
	 */
	void putNode(String syntaxId, String nodeId, PutNodeRequest node) throws NotFoundException, AlreadyPresentException;

	/**
	 * Delete the given node from the given syntax resource.
	 * 
	 * @param syntaxId
	 *            Identifies the syntax resource
	 * @param nodeId
	 *            Identifies the node within the syntax resource
	 * @throws NotFoundException
	 *             if either the syntax resource or the node does not exist.
	 */
	void deleteNode(String syntaxId, String nodeId) throws NotFoundException;
}
