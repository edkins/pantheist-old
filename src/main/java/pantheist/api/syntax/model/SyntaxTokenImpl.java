package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import javax.annotation.Nullable;
import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.assistedinject.Assisted;

final class SyntaxTokenImpl implements SyntaxToken
{
	private final SyntaxTokenType type;
	@Nullable
	private final String value;

	@Inject
	SyntaxTokenImpl(@Assisted @JsonProperty("type") final SyntaxTokenType type,
			@Nullable @Assisted("value") @JsonProperty("value") final String value)
	{
		this.type = checkNotNull(type);
		this.value = maybeCheckNotNull(type, value);
	}

	private String maybeCheckNotNull(final SyntaxTokenType type, final String value)
	{
		switch (type) {
		case literal:
		case regex:
			return checkNotNullOrEmpty(value);
		default:
			throw new IllegalArgumentException("Unrecognized token type: " + type);
		}
	}

	@Override
	public SyntaxTokenType type()
	{
		return type;
	}

	@Override
	public String value()
	{
		return value;
	}

}
