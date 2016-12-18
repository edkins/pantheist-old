package pantheist.api.syntax.backend;

import pantheist.common.except.AlreadyPresentException;
import pantheist.common.except.NotFoundException;

/**
 * Represents a handle to a particular resource, e.g. /syntax/json
 */
public interface ResourceRef
{
	/**
	 * Return a reference to the component collection with the given component
	 * type.
	 *
	 * Note that this can throw NotFoundException straight away if the component
	 * type is invalid. This is because the component types are hardcoded for a
	 * particular resource type: it doesn't need to look them up, and neither
	 * can you create new ones.
	 *
	 * Also not that it won't throw NotFoundException if the resource itself
	 * doesn't exist, because we want to save having to look up that sort of
	 * thing until later.
	 *
	 * @param componentTypeId
	 *            component type identifier
	 * @return reference to the corresponding component collection for this
	 *         resource
	 * @throws NotFounfException
	 *             if the component type is invalid for this resource type.
	 */
	ComponentCollectionRef componentType(String componentTypeId) throws NotFoundException;

	/**
	 * Create an empty resource at this location.
	 *
	 * There's no NotFoundException here because the parent resource type will
	 * always exist.
	 *
	 * @throws AlreadyPresentException
	 *             if the resource already exists.
	 */
	void create() throws AlreadyPresentException;

	/**
	 * Delete this resource.
	 *
	 * @throws NotFoundException
	 *             if the resource does not exist.
	 */
	void delete() throws NotFoundException;

	/**
	 * @return the path, e.g. /syntax/json
	 */
	String path();
}
