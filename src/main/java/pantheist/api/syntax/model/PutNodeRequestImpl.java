package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import pantheist.common.except.OtherPreconditions;

final class PutNodeRequestImpl implements PutNodeRequest
{
	private final SyntaxNodeType type;
	private final List<String> children;
	private final boolean updateExisting;

	PutNodeRequestImpl(@JsonProperty("type") final SyntaxNodeType type,
			@Nullable @JsonProperty("children") final List<String> children,
			@JsonProperty("updateExisting") final boolean updateExisting)
	{
		this.type = checkNotNull(type);
		this.children = checkChildren(type, children);
		this.updateExisting = updateExisting;
	}

	private static List<String> checkChildren(final SyntaxNodeType type, final List<String> children)
	{
		children.forEach(OtherPreconditions::checkNotNullOrEmpty);

		switch (type) {
		case literal:
			return OtherPreconditions.nullOrEmptyList(children);
		case zero_or_more:
		case one_or_more:
			return OtherPreconditions.copyOfSingleton(children);
		case sequence:
		case choice:
			return OtherPreconditions.copyOfTwoOrMore(children);
		default:
			throw new IllegalArgumentException("Cannot understand node type " + type);
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

	@Override
	public boolean updateExisting()
	{
		return updateExisting;
	}

}
