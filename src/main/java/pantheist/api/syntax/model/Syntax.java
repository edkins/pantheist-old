package pantheist.api.syntax.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	 * @return a list of nodes associated with this syntax resource
	 */
	@JsonProperty("nodes")
	List<SyntaxNode> nodes();
}
