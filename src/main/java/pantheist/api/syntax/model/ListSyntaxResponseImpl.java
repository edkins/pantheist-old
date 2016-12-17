package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;

final class ListSyntaxResponseImpl implements ListSyntaxResponse
{
	private final List<SyntaxMetadata> resources;

	@Inject
	ListSyntaxResponseImpl(@Assisted @JsonProperty("resources") final List<SyntaxMetadata> resources)
	{
		checkNotNull(resources);
		this.resources = ImmutableList.copyOf(resources);
	}

	@Override
	public List<SyntaxMetadata> resources()
	{
		return this.resources;
	}

}
