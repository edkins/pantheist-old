package pantheist.api.syntax.model;

import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;
import static pantheist.common.except.OtherPreconditions.copyOfNotNull;

import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.assistedinject.Assisted;

final class SyntaxImpl implements Syntax
{
	private final String path;
	private final String id;
	private final String name;
	private final List<SyntaxNode> nodes;

	@Inject
	SyntaxImpl(@Assisted("path") @JsonProperty("path") final String path,
			@Assisted("id") @JsonProperty("id") final String id,
			@Assisted("name") @JsonProperty("name") final String name,
			@Assisted @JsonProperty("nodes") final List<SyntaxNode> nodes)
	{
		this.path = checkNotNullOrEmpty(path);
		this.id = checkNotNullOrEmpty(id);
		this.name = checkNotNullOrEmpty(name);
		this.nodes = copyOfNotNull(nodes);
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
	public List<SyntaxNode> nodes()
	{
		return nodes;
	}

}
