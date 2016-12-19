package pantheist.api.syntax.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import pantheist.common.except.OtherPreconditions;

final class SyntaxDocPropertyImpl implements SyntaxDocProperty
{
	private final List<String> children;

	public SyntaxDocPropertyImpl(@JsonProperty("children") final List<String> children)
	{
		this.children = OtherPreconditions.copyOfOneOrMore(children);
	}

	@Override
	public List<String> children()
	{
		return children;
	}

}
