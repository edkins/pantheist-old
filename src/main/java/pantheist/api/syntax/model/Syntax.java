package pantheist.api.syntax.model;

import java.util.Map;
import java.util.SortedMap;

import com.fasterxml.jackson.annotation.JsonProperty;

import pantheist.api.syntax.backend.ComponentType;

public interface Syntax
{
	/**
	 * @return the path to this resource, e.g. /syntax/mysyntax
	 */
	@JsonProperty("path")
	String path();

	/**
	 * @return identifier for this syntax resource. Will be the last section of
	 *         the path.
	 */
	@JsonProperty("id")
	String id();

	/**
	 * @return a unique name for this resource that may contain capital letters
	 *         and spaces, e.g. "My Syntax"
	 */
	@JsonProperty("name")
	String name();

	/**
	 * This gives a map of all the components associated with the resource,
	 * indexed by component type and component id. It is also sorted by
	 * component id.
	 *
	 * Note that component id's are only unique within one particular component
	 * type.
	 *
	 * @return a map of the components associated with this resource
	 */
	@JsonProperty("components")
	Map<ComponentType, SortedMap<String, Object>> components();

	/**
	 * @param componentType
	 *            component type
	 * @param componentId
	 *            component id
	 * @param component
	 *            replacement component
	 * @return a new syntax object with this node added (or replaced if there's
	 *         already one with that type and id)
	 * @throws IllegalArgumentException
	 *             if the java type of the component is incompatible with what
	 *             ComponentType expects.
	 */
	<T> Syntax withComponent(ComponentType componentType, String componentId, T component);

	/**
	 * @param componentType
	 *            component type
	 * @param componentId
	 *            component id to remove
	 * @return a new syntax object without the component that has this id and
	 *         type. (Returns an identical syntax object if the id was not
	 *         present)
	 */
	Syntax withoutComponent(ComponentType componentType, String componentId);

}
