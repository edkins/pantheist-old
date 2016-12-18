package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import javax.annotation.Nullable;
import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.assistedinject.Assisted;

final class SyntaxTokenImpl implements SyntaxToken
{
	private final String path;
	private final String id;
	private final SyntaxTokenType type;
	@Nullable
	private final String value;

	@Inject
	SyntaxTokenImpl(@Assisted("path") @JsonProperty("path") final String path,
			@Assisted("id") @JsonProperty("id") final String id,
			@Assisted @JsonProperty("type") final SyntaxTokenType type,
			@Nullable @Assisted("value") @JsonProperty("value") final String value)
	{
		this.path = checkNotNullOrEmpty(path);
		this.id = checkNotNullOrEmpty(id);
		this.type = checkNotNull(type);
		this.value = value;
	}

	@Override
	public String path()
	{
		return path;
	}

	@Override
	public String id()
	{
		return id;
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
