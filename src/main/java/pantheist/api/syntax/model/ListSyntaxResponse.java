package pantheist.api.syntax.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ListSyntaxResponseImpl.class)
public interface ListSyntaxResponse
{
	@JsonProperty("resources")
	List<SyntaxMetadata> resources();
}
