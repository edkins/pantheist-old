package pantheist.api.generic.store;

import pantheist.api.syntax.model.IntolerantMap;
import pantheist.common.except.NotFoundException;

/**
 * Represents a resource that has been pulled out of the datastore into memory
 * so you can play with it. All components of the resource are loaded at the
 * same time.
 */
public interface OpenResource
{
	/**
	 * Return the collection of components of a particular type.
	 *
	 * The returned map is mutable but will barf if you try to store something
	 * with the wrong type.
	 *
	 * @return components organized by componentId.
	 * @throws NotFoundException
	 *             if the component type is invalid for this resource
	 */
	IntolerantMap components(String componentType) throws NotFoundException;
}
