package pantheist.api.syntax.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface SyntaxNode
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
	 * @return an identifier for the node, unique only within this syntax
	 *         resource.
	 */
	@JsonProperty("id")
	String id();

	/**
	 * The type determines how this syntax node is handled.
	 *
	 * literal: the id (which must be nonempty, and is assumed to be ASCII for
	 * now) represents the sequence of characters that are matched.
	 *
	 * zero_or_more: there must be one node id listed in children, and this node
	 * represents a sequence of zero or more of them.
	 *
	 * one_or_more: there must be one node id listed in children, and this node
	 * represents a sequence of one or more of them.
	 *
	 * sequence: there are at least 2 children, and this node represents them in
	 * sequence.
	 *
	 * choice: there are at least 2 children, and this node represents a choice
	 * between them.
	 *
	 * @return THe node type
	 */
	@JsonProperty("type")
	SyntaxNodeType type();

	/**
	 * Identifiers for child nodes. Not used for type "literal".
	 *
	 * @return List of node id's
	 */
	@JsonProperty("children")
	List<String> children();
}
