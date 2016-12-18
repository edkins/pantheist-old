package pantheist.api.syntax.model;

import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import java.util.SortedMap;
import java.util.TreeMap;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.assistedinject.Assisted;

import pantheist.common.except.OtherPreconditions;

final class SyntaxImpl implements Syntax
{
	private final String path;
	private final String id;
	private final String name;
	private final SortedMap<String, SyntaxNode> nodes;

	@Inject
	SyntaxImpl(@Assisted("path") @JsonProperty("path") final String path,
			@Assisted("id") @JsonProperty("id") final String id,
			@Assisted("name") @JsonProperty("name") final String name,
			@Assisted @JsonProperty("nodes") final SortedMap<String, SyntaxNode> nodes)
	{
		this.path = checkNotNullOrEmpty(path);
		this.id = checkNotNullOrEmpty(id);
		this.name = checkNotNullOrEmpty(name);
		this.nodes = OtherPreconditions.copyOfNotNullSorted(nodes);
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
	public String name()
	{
		return name;
	}

	@Override
	public SortedMap<String, SyntaxNode> nodes()
	{
		return nodes;
	}

	@Override
	public Syntax withNode(final SyntaxNode node)
	{
		final SortedMap<String, SyntaxNode> newNodes = new TreeMap<>(nodes);
		newNodes.put(node.id(), node);
		return new SyntaxImpl(path, id, name, newNodes);
	}

	@Override
	public Syntax withoutNode(final String nodeId)
	{
		final SortedMap<String, SyntaxNode> newNodes = new TreeMap<>(nodes);
		newNodes.remove(nodeId);
		return new SyntaxImpl(path, id, name, newNodes);
	}

}
