package pantheist.api.syntax.model;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

final class MutableSyntaxDocPropertiesImpl implements MutableSyntaxDocProperties
{
	@Nullable
	private SyntaxDocProperty root;
	@Nullable
	private SyntaxDocProperty whitespace;

	MutableSyntaxDocPropertiesImpl(@Nullable @JsonProperty("root") final SyntaxDocProperty root,
			@Nullable @JsonProperty("whitespace") final SyntaxDocProperty whitespace)
	{
		this.root = root;
		this.whitespace = whitespace;
	}

	static MutableSyntaxDocProperties ofNullable(final MutableSyntaxDocProperties p)
	{
		if (p == null)
		{
			return new MutableSyntaxDocPropertiesImpl(null, null);
		}
		return p;
	}

	@Override
	public SyntaxDocProperty root()
	{
		return root;
	}

	@Override
	public SyntaxDocProperty whitespace()
	{
		return whitespace;
	}

	@Override
	public void setRoot(@Nullable final SyntaxDocProperty root)
	{
		this.root = root;
	}

	@Override
	public void setWhitespace(@Nullable final SyntaxDocProperty whitespace)
	{
		this.whitespace = whitespace;
	}

}
