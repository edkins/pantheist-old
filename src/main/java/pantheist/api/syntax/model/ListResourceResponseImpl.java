package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;

final class ListResourceResponseImpl implements ListResourceResponse
{
	private final List<ResourceMetadata> resources;

	@Inject
	ListResourceResponseImpl(@Assisted @JsonProperty("resources") final List<ResourceMetadata> resources)
	{
		checkNotNull(resources);
		this.resources = ImmutableList.copyOf(resources);
	}

	@Override
	public List<ResourceMetadata> resources()
	{
		return this.resources;
	}

}
