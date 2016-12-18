package pantheist.api.syntax.backend;

import pantheist.common.except.NotFoundException;

public interface SyntaxBackend
{
	/**
	 * Return a handle to the collection of resources of a given resource type.
	 *
	 * Note that this can throw NotFoundException straight away if the resource
	 * type is invalid. This is because the resource types are hardcoded: it
	 * doesn't need to look them up, and neither can you create new ones.
	 *
	 * @param resourceTypeId
	 *            the resource type
	 * @return a reference to that resource type
	 * @throws NotFoundException
	 *             if the resource type is invalid.
	 */
	ResourceTypeRef resourceType(String resourceTypeId) throws NotFoundException;
}
