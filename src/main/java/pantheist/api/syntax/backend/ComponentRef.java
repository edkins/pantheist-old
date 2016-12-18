package pantheist.api.syntax.backend;

import pantheist.common.except.AlreadyPresentException;
import pantheist.common.except.NotFoundException;

/**
 * Represents a handle to a particular component, e.g. /syntax/json/token/%22
 *
 * The component may not have been created yet. If it hasn't then call create().
 */
public interface ComponentRef
{
	/**
	 * Deletes the component from the component collection.
	 *
	 * @throws NotFoundException
	 *             if this component does not exist.
	 */
	void delete() throws NotFoundException;

	/**
	 * Deletes the component from the component collection.
	 *
	 * @throws AlreadyPresentException
	 *             if the component already exists.
	 * @throws NotFoundException
	 *             if the parent resource does not exist.
	 * @throws IllegalArgumentException
	 *             if the data type does not agree with the corresponding
	 *             component type.
	 */
	void create(Object data) throws AlreadyPresentException, NotFoundException;

	/**
	 * Retrieves the data associated with this component.
	 *
	 * The type of object returned depends on the component type, but it will
	 * always be json-serializable.
	 *
	 * @return the component data
	 * @throws NotFoundException
	 *             if the component does not exist.
	 */
	Object getData() throws NotFoundException;

	/**
	 * @return the path, e.g. /syntax/json/token/%22
	 */
	String path();
}
