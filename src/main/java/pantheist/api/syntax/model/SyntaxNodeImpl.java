package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;

final class SyntaxNodeImpl implements SyntaxNode
{
	private final SyntaxNodeType type;
	private final List<String> children;

	@Inject
	SyntaxNodeImpl(@Assisted @JsonProperty("type") final SyntaxNodeType type,
			@Assisted @JsonProperty("children") final List<String> children)
	{
		this.type = checkNotNull(type);
		if (children == null)
		{
			this.children = ImmutableList.of();
		}
		else
		{
			this.children = ImmutableList.copyOf(children);
		}
	}

	@Override
	public SyntaxNodeType type()
	{
		return type;
	}

	@Override
	public List<String> children()
	{
		return children;
	}

}
