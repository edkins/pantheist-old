package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.assistedinject.Assisted;

final class SyntaxMetadataImpl implements SyntaxMetadata
{
	private final String path;
	private final String name;

	@Inject
	SyntaxMetadataImpl(@Assisted("path") @JsonProperty("path") final String path,
			@Assisted("name") @JsonProperty("name") final String name)
	{
		this.path = checkNotNull(path);
		this.name = checkNotNull(name);
	}

	@Override
	public String path()
	{
		return path;
	}

	@Override
	public String name()
	{
		return name;
	}

}
