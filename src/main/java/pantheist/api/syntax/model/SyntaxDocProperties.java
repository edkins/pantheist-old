package pantheist.api.syntax.model;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface SyntaxDocProperties
{
	@Nullable
	@JsonProperty("root")
	SyntaxDocProperty root();

	@Nullable
	@JsonProperty("whitespace")
	SyntaxDocProperty whitespace();
}
