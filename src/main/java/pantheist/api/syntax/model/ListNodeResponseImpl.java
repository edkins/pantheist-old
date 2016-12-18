package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.google.inject.assistedinject.Assisted;

final class ListComponentResponseImpl implements ListComponentResponse
{
	private final List<ListedComponent> components;

	@Inject
	ListComponentResponseImpl(@Assisted @JsonProperty("components") final Collection<ListedComponent> components)
	{
		checkNotNull(components);
		this.components = ImmutableList.copyOf(components);
	}

	@Override
	public List<ListedComponent> components()
	{
		return components;
	}

}
