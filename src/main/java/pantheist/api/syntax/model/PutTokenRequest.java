package pantheist.api.syntax.model;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = PutTokenRequestImpl.class)
public interface PutTokenRequest
{
	/**
	 * This is the type of the token, as described in {@link SyntaxToken}
	 *
	 * @return the requested token type
	 */
	@JsonProperty("type")
	SyntaxTokenType type();

	/**
	 * For regex tokens, this specifies the token regex.
	 */
	@Nullable
	@JsonProperty("value")
	String value();

	/**
	 * @return whether this request is supposed to update an existing token.
	 */
	boolean updateExisting();

}
