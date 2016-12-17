package pantheist.api.syntax.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = SyntaxMetadata.class)
public interface SyntaxMetadata
{
	/**
	 * @return the path to this resource, e.g. /syntax/mysyntax
	 */
	@JsonProperty("path")
	String path();

	/**
	 * @return a unique name for this resource that may contain capital letters
	 *         and spaces, e.g. "My Syntax"
	 */
	@JsonProperty("name")
	String name();
}
