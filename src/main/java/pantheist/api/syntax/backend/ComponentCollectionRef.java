package pantheist.api.syntax.backend;

import pantheist.api.syntax.model.ListComponentResponse;
import pantheist.common.except.NotFoundException;

/**
 * Return a reference to a component collection, i.e. a set of components
 * belonging to a particular resource that share a component type.
 *
 * e.g. /syntax/json/token
 */
public interface ComponentCollectionRef
{
	/**
	 * Return a reference to the component with the given id. Note that the id
	 * is only unique within this component collection.
	 *
	 * @param componentId
	 *            component identifier
	 * @return reference to the corresponding component
	 */
	ComponentRef component(String componentId);

	/**
	 * Returns a sorted immutable list containing component IDsand corresponding
	 * data objects
	 *
	 * The component IDs can be fed to component() but note that this will no
	 * longer be an atomic operation.
	 *
	 * @return list of resource IDs.
	 * @throws NotFoundException
	 *             if the parent resource is not valid.
	 */
	ListComponentResponse listComponents() throws NotFoundException;

	/**
	 * @return the path, e.g. /syntax/json/token
	 */
	String path();
}
