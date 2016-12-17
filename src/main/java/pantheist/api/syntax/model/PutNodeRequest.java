package pantheist.api.syntax.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = PutNodeRequestImpl.class)
public interface PutNodeRequest
{
	/**
	 * This is the type of the node, as described in {@link SyntaxNode}
	 *
	 * @return the requested node type
	 */
	@JsonProperty("type")
	SyntaxNodeType type();

	/**
	 * Identifiers for child nodes. Not used for type "literal".
	 *
	 * It is ok to specify node id's that don't exist yet, it will just mean the
	 * syntax can't be used until they get created.
	 *
	 * Must be:
	 *
	 * - an empty list for type "literal"
	 *
	 * - a list of 1 for type "zero_or_more" or "one_or_more"
	 *
	 * - a list of 2 or more for type "sequence" or "choice"
	 *
	 * @return the requested child node id's
	 */
	@JsonProperty("children")
	List<String> children();

	/**
	 * @return whether this request is supposed to update an existing node.
	 */
	boolean updateExisting();

}
