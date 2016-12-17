package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;

final class ListNodeResponseImpl implements ListNodeResponse
{
	private final List<SyntaxNode> nodes;

	@Inject
	ListNodeResponseImpl(@Assisted @JsonProperty("nodes") final List<SyntaxNode> nodes)
	{
		checkNotNull(nodes);
		this.nodes = ImmutableList.copyOf(nodes);
	}

	@Override
	public List<SyntaxNode> nodes()
	{
		return nodes;
	}

}
