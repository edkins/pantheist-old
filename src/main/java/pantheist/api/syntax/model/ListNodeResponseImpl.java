package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;

final class ListNodeResponseImpl implements ListNodeResponse
{
	private final List<SyntaxNode> components;

	@Inject
	ListNodeResponseImpl(@Assisted @JsonProperty("components") final Collection<SyntaxNode> components)
	{
		checkNotNull(components);
		this.components = ImmutableList.copyOf(components);
	}

	@Override
	public List<SyntaxNode> components()
	{
		return components;
	}

}
