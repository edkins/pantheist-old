package pantheist.api.syntax.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = SyntaxOperatorImpl.class)
public interface SyntaxOperator
{
	@JsonProperty("type")
	SyntaxOperatorType type();

	@JsonProperty("operator")
	String operator();

	@JsonProperty("level")
	int level();

	@JsonProperty("containedIn")
	String containedIn();
}
