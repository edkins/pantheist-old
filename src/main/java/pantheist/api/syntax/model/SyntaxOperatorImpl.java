package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

final class SyntaxOperatorImpl implements SyntaxOperator
{
	private final SyntaxOperatorType type;
	private final String operator;
	private final int level;
	private final String containedIn;

	SyntaxOperatorImpl(@JsonProperty("type") final SyntaxOperatorType type,
			@JsonProperty("operator") final String operator,
			@JsonProperty("level") final Integer level,
			@JsonProperty("containedIn") final String containedIn)
	{
		this.type = checkNotNull(type);
		this.operator = checkNotNullOrEmpty(operator);
		this.level = checkNotNull(level);
		this.containedIn = checkNotNullOrEmpty(containedIn);
	}

	@Override
	public SyntaxOperatorType type()
	{
		return type;
	}

	@Override
	public String operator()
	{
		return operator;
	}

	@Override
	public int level()
	{
		return level;
	}

	@Override
	public String containedIn()
	{
		return containedIn;
	}

}
