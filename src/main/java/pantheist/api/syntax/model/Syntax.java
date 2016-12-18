package pantheist.api.syntax.model;

import java.util.SortedMap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import pantheist.api.generic.store.OpenResource;

@JsonDeserialize(as = SyntaxImpl.class)
public interface Syntax extends OpenResource
{
	@JsonProperty("nodes")
	SortedMap<String, SyntaxNode> nodes();

	@JsonProperty("tokens")
	SortedMap<String, SyntaxToken> tokens();
}
