package pantheist.api.syntax.model;

import java.util.SortedMap;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonProperty;

import pantheist.common.except.NotFoundException;

final class SyntaxImpl implements Syntax
{
	private final SortedMap<String, SyntaxNode> nodes;
	private final SortedMap<String, SyntaxToken> tokens;

	SyntaxImpl(@JsonProperty("nodes") final SortedMap<String, SyntaxNode> nodes,
			@JsonProperty("tokens") final SortedMap<String, SyntaxToken> tokens)
	{
		if (nodes == null)
		{
			this.nodes = new TreeMap<>();
		}
		else
		{
			this.nodes = nodes;
		}
		if (tokens == null)
		{
			this.tokens = new TreeMap<>();
		}
		else
		{
			this.tokens = tokens;
		}
	}

	@Override
	public IntolerantMap components(final String componentType) throws NotFoundException
	{
		switch (componentType) {
		case "node":
			return new IntolerantMapImpl<>(nodes(), SyntaxNode.class);
		case "token":
			return new IntolerantMapImpl<>(tokens(), SyntaxToken.class);
		default:
			throw new NotFoundException(componentType);
		}
	}

	@Override
	public SortedMap<String, SyntaxNode> nodes()
	{
		return nodes;
	}

	@Override
	public SortedMap<String, SyntaxToken> tokens()
	{
		return tokens;
	}
}
