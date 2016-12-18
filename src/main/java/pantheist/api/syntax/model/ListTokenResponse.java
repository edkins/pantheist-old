package pantheist.api.syntax.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface ListTokenResponse
{
	/**
	 * @return a list of tokens
	 */
	@JsonProperty("components")
	List<SyntaxToken> components();
}
