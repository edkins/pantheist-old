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
	private final SortedMap<String, SyntaxToken> tokens;

	@Inject
	SyntaxImpl(@Assisted("path") @JsonProperty("path") final String path,
			@Assisted("id") @JsonProperty("id") final String id,
			@Assisted("name") @JsonProperty("name") final String name,
			@Assisted @JsonProperty("nodes") final SortedMap<String, SyntaxNode> nodes,
			@Assisted @JsonProperty("tokens") final SortedMap<String, SyntaxToken> tokens)
	{
		this.path = checkNotNullOrEmpty(path);
		this.id = checkNotNullOrEmpty(id);
		this.name = checkNotNullOrEmpty(name);
		this.nodes = OtherPreconditions.copyOfNotNullSorted(nodes);
		this.tokens = OtherPreconditions.copyOfNotNullSorted(tokens);
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
		return new SyntaxImpl(path, id, name, newNodes, tokens);
	}

	@Override
	public Syntax withoutNode(final String nodeId)
	{
		final SortedMap<String, SyntaxNode> newNodes = new TreeMap<>(nodes);
		newNodes.remove(nodeId);
		return new SyntaxImpl(path, id, name, newNodes, tokens);
	}

	@Override
	public SortedMap<String, SyntaxToken> tokens()
	{
		return tokens;
	}

	@Override
	public Syntax withToken(final SyntaxToken token)
	{
		final SortedMap<String, SyntaxToken> newTokens = new TreeMap<>(tokens);
		newTokens.put(token.id(), token);
		return new SyntaxImpl(path, id, name, nodes, newTokens);
	}

	@Override
	public Syntax withoutToken(final String tokenId)
	{
		final SortedMap<String, SyntaxToken> newTokens = new TreeMap<>(tokens);
		newTokens.remove(tokenId);
		return new SyntaxImpl(path, id, name, nodes, newTokens);
	}

}
