package pantheist.api.syntax.model;

import static pantheist.common.except.OtherPreconditions.checkNotNullOrEmpty;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.assistedinject.Assisted;

final class SyntaxMetadataImpl implements SyntaxMetadata
{
	private final String path;
	private final String id;
	private final String name;

	@Inject
	SyntaxMetadataImpl(@Assisted("path") @JsonProperty("path") final String path,
			@Assisted("id") @JsonProperty("id") final String id,
			@Assisted("name") @JsonProperty("name") final String name)
	{
		this.path = checkNotNullOrEmpty(path);
		this.id = checkNotNullOrEmpty(id);
		this.name = checkNotNullOrEmpty(name);
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

}
