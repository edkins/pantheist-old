package pantheist.api.syntax.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface SyntaxMetadata
{
	/**
	 * @return the path to this resource, e.g. /syntax/mysyntax/
	 */
	@JsonProperty("path")
	String path();
}
