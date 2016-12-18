package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

final class PutTokenRequestImpl implements PutTokenRequest
{
	private final SyntaxTokenType type;
	@Nullable
	private final String value;
	private final boolean updateExisting;

	PutTokenRequestImpl(@JsonProperty("type") final SyntaxTokenType type,
			@JsonProperty("value") final String value,
			@JsonProperty("updateExisting") final boolean updateExisting)
	{
		this.type = checkNotNull(type);
		this.value = value;
		this.updateExisting = updateExisting;
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

	@Override
	public boolean updateExisting()
	{
		return updateExisting;
	}

}
