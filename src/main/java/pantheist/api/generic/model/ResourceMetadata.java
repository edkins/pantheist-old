package pantheist.api.generic.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ResourceMetadataImpl.class)
public interface ResourceMetadata
{
	/**
	 * @return the path to this resource, e.g. /syntax/mysyntax
	 */
	@JsonProperty("path")
	String path();

	/**
	 * @return the id of this resource, which will be the last part of the path.
	 */
	@JsonProperty("id")
	String id();

	/**
	 * @return a unique name for this resource that may contain capital letters
	 *         and spaces, e.g. "My Syntax"
	 */
	@JsonProperty("name")
	String name();
}
