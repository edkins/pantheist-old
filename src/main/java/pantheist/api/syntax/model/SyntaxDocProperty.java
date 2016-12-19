package pantheist.api.syntax.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = SyntaxDocPropertyImpl.class)
public interface SyntaxDocProperty
{
	/**
	 * Identifiers for child nodes.
	 *
	 * @return List of node id's
	 */
	@JsonProperty("children")
	List<String> children();

}
