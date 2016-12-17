package pantheist.api.syntax.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ListNodeResponseImpl.class)
public interface ListNodeResponse
{
	/**
	 * @return a list of nodes
	 */
	@JsonProperty("components")
	List<SyntaxNode> components();
}
