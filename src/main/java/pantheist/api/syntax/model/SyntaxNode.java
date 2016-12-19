package pantheist.api.syntax.model;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = SyntaxNodeImpl.class)
public interface SyntaxNode
{
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
	 * For literal tokens, this is the value to be matched. For regex tokens,
	 * this is the regex. The regex for "regex" tokens.
	 *
	 * For other node types this is unused and may be null.
	 *
	 * @return string representing how this token behaves
	 */
	@Nullable
	@JsonProperty("value")
	String value();

	/**
	 * Identifiers for child nodes.
	 *
	 * An empty list will be returned for types "literal" or "regex"
	 *
	 * @return List of node id's
	 */
	@JsonProperty("children")
	List<String> children();
}
