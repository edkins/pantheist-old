package pantheist.api.syntax.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface ListSyntaxResponse
{
	@JsonProperty("resources")
	List<SyntaxMetadata> resources();
}
