package pantheist.api.syntax.model;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.assistedinject.Assisted;

final class SyntaxMetadataImpl implements SyntaxMetadata
{
	private final String path;

	@Inject
	SyntaxMetadataImpl(@Assisted("path") @JsonProperty("path") final String path)
	{
		this.path = checkNotNull(path);
	}

	@Override
	public String path()
	{
		return this.path;
	}

}
