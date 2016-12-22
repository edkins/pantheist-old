package pantheist.api.syntax.model;

import java.util.SortedMap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import pantheist.api.generic.store.OpenResource;

@JsonDeserialize(as = SyntaxImpl.class)
public interface Syntax extends OpenResource
{
	@JsonProperty("node")
	SortedMap<String, SyntaxNode> node();

	@JsonProperty("operator")
	SortedMap<String, SyntaxOperator> operator();

	@JsonProperty("doc")
	SyntaxDocProperties doc();
}
