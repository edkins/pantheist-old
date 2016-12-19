package pantheist.api.syntax.model;

import java.util.SortedMap;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonProperty;

import pantheist.common.except.NotFoundException;

final class SyntaxImpl implements Syntax
{
	private final SortedMap<String, SyntaxNode> nodes;

	SyntaxImpl(@JsonProperty("nodes") final SortedMap<String, SyntaxNode> nodes)
	{
		if (nodes == null)
		{
			this.nodes = new TreeMap<>();
		}
		else
		{
			this.nodes = nodes;
		}
	}

	@Override
	public IntolerantMap components(final String componentType) throws NotFoundException
	{
		switch (componentType) {
		case "node":
			return new IntolerantMapImpl<>(nodes(), SyntaxNode.class);
		default:
			throw new NotFoundException(componentType);
		}
	}

	@Override
	public SortedMap<String, SyntaxNode> nodes()
	{
		return nodes;
	}
}
