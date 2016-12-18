package pantheist.api.syntax.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface ListedComponent
{
	/**
	 * @return the path to this component, e.g. /syntax/mysyntax/node/%7B
	 */
	@JsonProperty("path")
	String path();

	/**
	 * This will be the (url-decoded) last section of the path, e.g. "{" in the
	 * example above.
	 *
	 * @return an identifier for the component, unique only within this syntax
	 *         resource.
	 */
	@JsonProperty("id")
	String id();

	/**
	 * Some data, specific to the component type. Will be json-serializable.
	 *
	 * @return component data
	 */
	@JsonProperty("data")
	Object data();
}
