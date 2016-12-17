package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;
import static pantheist.common.except.OtherPreconditions.copyOfNotNull;

import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.assistedinject.Assisted;

final class SyntaxNodeImpl implements SyntaxNode
{
	private final String path;
	private final String id;
	private final SyntaxNodeType type;
	private final List<String> children;

	@Inject
	SyntaxNodeImpl(@Assisted("path") @JsonProperty("path") final String path,
			@Assisted("id") @JsonProperty("id") final String id,
			@Assisted @JsonProperty("type") final SyntaxNodeType type,
			@Assisted @JsonProperty("children") final List<String> children)
	{
		this.path = checkNotNullOrEmpty(path);
		this.id = checkNotNullOrEmpty(id);
		this.type = checkNotNull(type);
		this.children = copyOfNotNull(children);
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
	public SyntaxNodeType type()
	{
		return type;
	}

	@Override
	public List<String> children()
	{
		return children;
	}

}
