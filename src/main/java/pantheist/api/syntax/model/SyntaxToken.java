package pantheist.api.syntax.model;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface SyntaxToken
{
	/**
	 * @return the path to this component, e.g. /syntax/mysyntax/token/%7B
	 */
	@JsonProperty("path")
	String path();

	/**
	 * This will be the (url-decoded) last section of the path, e.g. "{" in the
	 * example above.
	 *
	 * @return an identifier for the token, unique only within this syntax
	 *         resource.
	 */
	@JsonProperty("id")
	String id();

	/**
	 * The type determines how this syntax token is handled.
	 *
	 * literal: the id (which must be nonempty, and is assumed to be ASCII for
	 * now) represents the sequence of characters that are matched.
	 *
	 * regex: the value is interpreted as a regex specifying the sequence of
	 * characters to be matched.
	 *
	 * @return The token type
	 */
	@JsonProperty("type")
	SyntaxTokenType type();

	/**
	 * The regex for "regex" tokens.
	 *
	 * @return string representing how this token behaves
	 */
	@Nullable
	@JsonProperty("value")
	String value();

}
