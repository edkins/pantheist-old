package pantheist.api.syntax.model;

import java.util.SortedMap;

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
	 * @return a map of nodes associated with this syntax resource, indexed by
	 *         nodeId.
	 */
	@JsonProperty("nodes")
	SortedMap<String, SyntaxNode> nodes();

	/**
	 * @param node
	 *            replacement node
	 * @return a new syntax object with this node added (or replaced if there's
	 *         already one with that id)
	 */
	Syntax withNode(SyntaxNode node);

	/**
	 * @param nodeId
	 *            node id to remove
	 * @return a new syntax object without the node that has this id. (Returns
	 *         an identical syntax object if the id was not present)
	 */
	Syntax withoutNode(String nodeId);
}
