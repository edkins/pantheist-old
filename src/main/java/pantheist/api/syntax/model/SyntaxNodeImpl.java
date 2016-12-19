package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import pantheist.common.except.OtherPreconditions;

final class SyntaxNodeImpl implements SyntaxNode
{
	private final SyntaxNodeType type;
	@Nullable
	private final String value;
	private final List<String> children;

	SyntaxNodeImpl(@JsonProperty("type") final SyntaxNodeType type,
			@JsonProperty("value") final String value,
			@JsonProperty("children") final List<String> children)
	{
		this.type = checkNotNull(type);
		this.value = maybeNotNull(type, value);
		this.children = checkChildCount(type, children);
	}

	private static List<String> checkChildCount(final SyntaxNodeType type, final List<String> children)
	{
		switch (type) {
		case literal:
		case regex:
			return OtherPreconditions.nullOrEmptyList(children);
		case zero_or_more:
		case one_or_more:
			return OtherPreconditions.copyOfSingleton(children);
		case sequence:
		case choice:
			return OtherPreconditions.copyOfOneOrMore(children);
		default:
			throw new IllegalArgumentException("Unknown node type " + type);
		}
	}

	private static String maybeNotNull(final SyntaxNodeType type, final String value)
	{
		switch (type) {
		case literal:
			return checkNotNullOrEmpty(value);
		case regex:
			checkNotNullOrEmpty(value);
			Pattern.compile(value);
			return value;
		case zero_or_more:
		case one_or_more:
		case sequence:
		case choice:
			if (value != null)
			{
				throw new IllegalArgumentException("Value must be null for node type " + type);
			}
			return null;
		default:
			throw new IllegalArgumentException("Unknown node type " + type);
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
	public String value()
	{
		return value;
	}

}
