package pantheist.api.syntax.model;

import static pantheist.common.except.OtherPreconditions.copyOfNotNull;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.assistedinject.Assisted;

final class ListTokenResponseImpl implements ListTokenResponse
{
	private final List<SyntaxToken> components;

	@Inject
	ListTokenResponseImpl(@Assisted @JsonProperty("components") final Collection<SyntaxToken> components)
	{
		this.components = copyOfNotNull(components);
	}

	@Override
	public List<SyntaxToken> components()
	{
		return components;
	}

}
