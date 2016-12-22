package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.List;

import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import pantheist.common.except.OtherPreconditions;

final class SyntaxNodeImpl implements SyntaxNode
{
	private final SyntaxNodeType type;
	@Nullable
	private final String value;
	private final List<String> children;
	private final List<String> exceptions;

	SyntaxNodeImpl(@JsonProperty("type") final SyntaxNodeType type,
			@JsonProperty("value") final String value,
			@JsonProperty("children") final List<String> children,
			@JsonProperty("exceptions") final List<String> exceptions)
	{
		this.type = checkNotNull(type);
		this.value = maybeNotNull(type, value);
		this.children = checkChildren(type, children);
		this.exceptions = checkExceptions(type, exceptions);
	}

	private static List<String> checkExceptions(final SyntaxNodeType type, final List<String> exceptions)
	{
		final List<String> result = checkExceptionCount(type, exceptions);
		for (final String child : result)
		{
			checkChild(type, child);
		}
		return result;
	}

	private static List<String> checkChildren(final SyntaxNodeType type, final List<String> children)
	{
		final List<String> result = checkChildCount(type, children);
		for (final String child : result)
		{
			checkChild(type, child);
		}
		return result;
	}

	private static void checkChild(final SyntaxNodeType type, final String child)
	{
		checkNotNullOrEmpty(child);
		switch (type) {
		case single_character:
			SingleCharMatchers.fromString(child);
		default:
			// nothing to check
		}
	}

	private static List<String> checkExceptionCount(final SyntaxNodeType type, final List<String> exceptions)
	{
		switch (type) {
		case single_character:
			return OtherPreconditions.emptyIfNull(exceptions);
		default:
			return OtherPreconditions.nullOrEmptyList(exceptions);
		}
	}

	private static List<String> checkChildCount(final SyntaxNodeType type, final List<String> children)
	{
		switch (type) {
		case literal:
			return OtherPreconditions.nullOrEmptyList(children);
		case glued_zero_or_more:
		case glued_one_or_more:
		case zero_or_more:
		case one_or_more:
			return OtherPreconditions.copyOfSingleton(children);
		case single_character:
		case glued_sequence:
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
		case single_character:
		case glued_zero_or_more:
		case glued_one_or_more:
		case glued_sequence:
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

	@Override
	public List<String> exceptions()
	{
		return exceptions;
	}

}
